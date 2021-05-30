package pxf.toolkit.extension.office.manager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.concurrent.NamedThreadFactory;
import pxf.toolkit.basic.concurrent.SuspendableThreadPoolExecutor;
import pxf.toolkit.extension.office.OfficeTask;

/**
 * Office管理
 *
 * @author potatoxf
 * @date 2021/4/20
 */
class PooledOfficeManager implements OfficeManager, OfficeConnectionEventListener {
  private static final Logger LOG = LoggerFactory.getLogger(PooledOfficeManager.class);
  private final OfficeConnectionConfiguration settings;
  private final ManagedOfficeProcess managedOfficeProcess;
  private final SuspendableThreadPoolExecutor taskExecutor;
  private volatile boolean stopping = false;
  private int taskCount;
  private Future<?> currentTask;

  public PooledOfficeManager(UnoUrl unoUrl) {
    this(new OfficeConnectionConfiguration(unoUrl));
  }

  public PooledOfficeManager(OfficeConnectionConfiguration settings) {
    this.settings = settings;
    managedOfficeProcess = new ManagedOfficeProcess(settings);
    managedOfficeProcess.getConnection().addConnectionEventListener(this);
    taskExecutor =
        new SuspendableThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new NamedThreadFactory("OfficeTaskThread"));
  }

  @Override
  public void connected(OfficeConnectionEvent event) {
    taskCount = 0;
    taskExecutor.setAvailable(true);
  }

  @Override
  public void disconnected(OfficeConnectionEvent event) {
    taskExecutor.setAvailable(false);
    if (stopping) {
      // expected
      stopping = false;
    } else {
      if (LOG.isWarnEnabled()) {
        LOG.warn("connection lost unexpectedly; attempting restart");
      }
      if (currentTask != null) {
        currentTask.cancel(true);
      }
      managedOfficeProcess.restartDueToLostConnection();
    }
  }

  @Override
  public void execute(final OfficeTask task) throws OfficeException {
    Future<?> futureTask =
        taskExecutor.submit(
            () -> {
              if (settings.getMaxTasksPerProcess() > 0
                  && ++taskCount == settings.getMaxTasksPerProcess() + 1) {
                if (LOG.isInfoEnabled()) {
                  LOG.info(
                      String.format(
                          "reached limit of %d maxTasksPerProcess: restarting",
                          settings.getMaxTasksPerProcess()));
                }
                taskExecutor.setAvailable(false);
                stopping = true;
                managedOfficeProcess.restartAndWait();
                // FIXME taskCount will be 0 rather than 1 at this point
              }
              task.execute(managedOfficeProcess.getConnection());
            });
    currentTask = futureTask;
    try {
      futureTask.get(settings.getTaskExecutionTimeout(), TimeUnit.MILLISECONDS);
    } catch (TimeoutException timeoutException) {
      managedOfficeProcess.restartDueToTaskTimeout();
      throw new OfficeException("task did not complete within timeout", timeoutException);
    } catch (ExecutionException executionException) {
      if (executionException.getCause() instanceof OfficeException) {
        throw (OfficeException) executionException.getCause();
      } else {
        throw new OfficeException("task failed", executionException.getCause());
      }
    } catch (Exception exception) {
      throw new OfficeException("task failed", exception);
    }
  }

  @Override
  public void start() throws OfficeException {
    managedOfficeProcess.startAndWait();
  }

  @Override
  public void stop() throws OfficeException {
    taskExecutor.setAvailable(false);
    stopping = true;
    taskExecutor.shutdownNow();
    managedOfficeProcess.stopAndWait();
  }

  @Override
  public boolean isRunning() {
    return managedOfficeProcess.isConnected();
  }
}
