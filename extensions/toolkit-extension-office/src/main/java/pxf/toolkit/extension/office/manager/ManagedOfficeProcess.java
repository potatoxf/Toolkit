package pxf.toolkit.extension.office.manager;

import com.sun.star.frame.XDesktop;
import com.sun.star.lang.DisposedException;
import com.sun.star.uno.UnoRuntime;
import java.net.ConnectException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import pxf.toolkit.basic.concurrent.NamedThreadFactory;
import pxf.toolkit.basic.exception.RetryException;
import pxf.toolkit.basic.exception.TemporaryException;
import pxf.toolkit.basic.function.Retryable;
import pxf.toolkit.extension.office.OfficeEnvironment;

class ManagedOfficeProcess implements Retryable {

  private static final Integer EXIT_CODE_NEW_INSTALLATION = 81;

  private final OfficeConnectionConfiguration settings;

  private final OfficeProcess process;
  private final OfficeConnection connection;
  private final Logger logger = Logger.getLogger(getClass().getName());
  private final ExecutorService executor =
      Executors.newSingleThreadExecutor(new NamedThreadFactory("OfficeProcessThread"));

  public ManagedOfficeProcess(OfficeConnectionConfiguration settings) throws OfficeException {
    this.settings = settings;
    process =
        new OfficeProcess(
            settings.getOfficeHome(),
            settings.getUnoUrl(),
            settings.getRunAsArgs(),
            settings.getTemplateProfileDir(),
            settings.getWorkDir(),
            settings.getProcessManager());
    connection = new OfficeConnection(settings.getUnoUrl());
  }

  public OfficeConnection getConnection() {
    return connection;
  }

  public void startAndWait() throws OfficeException {
    Future<?> future =
        executor.submit(
            () -> {
              try {
                doStartProcessAndConnect();
              } catch (Throwable throwable) {
                throwable.printStackTrace();
              }
            });
    try {
      future.get();
    } catch (Exception exception) {
      throw new OfficeException("failed to start and connect", exception);
    }
  }

  public void stopAndWait() throws OfficeException {
    Future<?> future =
        executor.submit(
            () -> {
              try {
                doStopProcess();
              } catch (Throwable throwable) {
                throwable.printStackTrace();
              }
            });
    try {
      future.get();
    } catch (Exception exception) {
      throw new OfficeException("failed to start and connect", exception);
    }
  }

  public void restartAndWait() {
    Future<?> future =
        executor.submit(
            () -> {
              try {
                doStopProcess();
                doStartProcessAndConnect();
              } catch (Throwable throwable) {
                throwable.printStackTrace();
              }
            });
    try {
      future.get();
    } catch (Exception exception) {
      throw new OfficeException("failed to restart", exception);
    }
  }

  public void restartDueToTaskTimeout() {
    executor.execute(
        () -> {
          try {
            doTerminateProcess();
          } catch (Throwable throwable) {
            throwable.printStackTrace();
          }
          // will cause unexpected disconnection and subsequent restart
        });
  }

  public void restartDueToLostConnection() {
    executor.execute(
        () -> {
          try {
            doEnsureProcessExited();
            doStartProcessAndConnect();
          } catch (Throwable officeException) {
            logger.log(Level.SEVERE, "could not restart process", officeException);
          }
        });
  }

  private void doStartProcessAndConnect() throws Throwable {
    try {
      process.start();
      execute(settings.getRetryInterval(), settings.getRetryTimeout());
    } catch (Exception exception) {
      throw new OfficeException("could not establish connection", exception);
    }
  }

  @Override
  public void attempt() throws Throwable {
    try {
      connection.connect();
    } catch (ConnectException connectException) {
      Integer exitCode = process.getExitCode();
      if (exitCode == null) {
        // process is running; retry later
        throw new TemporaryException(connectException);
      } else if (exitCode.equals(EXIT_CODE_NEW_INSTALLATION)) {
        // restart and retry later
        // see http://code.google.com/p/jodconverter/issues/detail?id=84
        logger.log(Level.WARNING, "office process died with exit code 81; restarting it");
        process.start(true);
        throw new TemporaryException(connectException);
      } else {
        throw new OfficeException("office process died with exit code " + exitCode);
      }
    }
  }

  private void doStopProcess() throws Throwable {
    try {
      XDesktop desktop =
          UnoRuntime.queryInterface(
              XDesktop.class, connection.getService(OfficeEnvironment.SERVICE_DESKTOP));
      desktop.terminate();
    } catch (DisposedException disposedException) {
      // expected
    } catch (Exception exception) {
      // in case we can't get hold of the desktop
      doTerminateProcess();
    }
    doEnsureProcessExited();
  }

  private void doEnsureProcessExited() throws Throwable {
    try {
      int exitCode = process.getExitCode(settings.getRetryInterval(), settings.getRetryTimeout());
      logger.info("process exited with code " + exitCode);
    } catch (RetryException e) {
      doTerminateProcess();
    }
    process.deleteProfileDir();
  }

  private void doTerminateProcess() throws Throwable {
    try {
      int exitCode =
          process.forciblyTerminate(settings.getRetryInterval(), settings.getRetryTimeout());
      logger.info("process forcibly terminated with code " + exitCode);
    } catch (Exception exception) {
      throw new OfficeException("could not terminate process", exception);
    }
  }

  boolean isConnected() {
    return connection.isConnected();
  }
}
