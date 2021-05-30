package pxf.toolkit.extension.office.manager;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.extension.office.OfficeTask;
import pxf.toolkit.extension.system.process.ProcessManager;

class ProcessPoolOfficeManager implements OfficeManager {
  private static final Logger LOG = LoggerFactory.getLogger(ProcessPoolOfficeManager.class);
  private final BlockingQueue<PooledOfficeManager> pool;
  private final PooledOfficeManager[] pooledManagers;
  private final long taskQueueTimeout;
  private volatile boolean running = false;

  public ProcessPoolOfficeManager(
      File officeHome,
      UnoUrl[] unoUrls,
      String[] runAsArgs,
      String templateProfileDir,
      String workDir,
      long retryTimeout,
      long taskQueueTimeout,
      long taskExecutionTimeout,
      int maxTasksPerProcess,
      ProcessManager processManager) {
    this.taskQueueTimeout = taskQueueTimeout;
    pool = new ArrayBlockingQueue<>(unoUrls.length);
    pooledManagers = new PooledOfficeManager[unoUrls.length];
    for (int i = 0; i < unoUrls.length; i++) {
      OfficeConnectionConfiguration settings = new OfficeConnectionConfiguration(unoUrls[i]);
      settings.setRunAsArgs(runAsArgs);
      settings.setTemplateProfileDir(templateProfileDir);
      settings.setWorkDir(workDir);
      settings.setOfficeHome(officeHome);
      settings.setRetryTimeout(retryTimeout);
      settings.setTaskExecutionTimeout(taskExecutionTimeout);
      settings.setMaxTasksPerProcess(maxTasksPerProcess);
      settings.setProcessManager(processManager);
      pooledManagers[i] = new PooledOfficeManager(settings);
    }
    if (LOG.isInfoEnabled()) {
      LOG.info(
          String.format(
              "ProcessManager implementation is %s", processManager.getClass().getSimpleName()));
    }
  }

  @Override
  public synchronized void start() throws OfficeException {
    for (PooledOfficeManager pooledManager : pooledManagers) {
      pooledManager.start();
      releaseManager(pooledManager);
    }
    running = true;
  }

  @Override
  public void execute(OfficeTask task) throws IllegalStateException, OfficeException {
    if (!running) {
      throw new IllegalStateException("this OfficeManager is currently stopped");
    }
    PooledOfficeManager manager = null;
    try {
      manager = acquireManager();
      if (manager == null) {
        throw new OfficeException("no office manager available");
      }
      manager.execute(task);
    } finally {
      if (manager != null) {
        releaseManager(manager);
      }
    }
  }

  @Override
  public synchronized void stop() throws OfficeException {
    running = false;
    if (LOG.isInfoEnabled()) {
      LOG.info("stopping");
    }
    pool.clear();
    for (PooledOfficeManager pooledManager : pooledManagers) {
      pooledManager.stop();
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("stopped");
    }
  }

  private PooledOfficeManager acquireManager() {
    try {
      return pool.poll(taskQueueTimeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException interruptedException) {
      throw new OfficeException("interrupted", interruptedException);
    }
  }

  private void releaseManager(PooledOfficeManager manager) {
    try {
      pool.put(manager);
    } catch (InterruptedException interruptedException) {
      throw new OfficeException("interrupted", interruptedException);
    }
  }

  @Override
  public boolean isRunning() {
    return running;
  }
}
