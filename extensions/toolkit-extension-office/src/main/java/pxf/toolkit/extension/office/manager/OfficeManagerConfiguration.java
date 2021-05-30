package pxf.toolkit.extension.office.manager;

import java.io.File;
import pxf.toolkit.basic.constants.impl.LocalConnectionProtocol;
import pxf.toolkit.basic.util.Is;
import pxf.toolkit.extension.office.OfficeEnvironment;
import pxf.toolkit.extension.system.process.ProcessManager;
import pxf.toolkit.extension.system.process.impl.PureJavaProcessManager;
/**
 * 默认Office管理配置
 *
 * @author potatoxf
 * @date 2021/4/29
 */
public class OfficeManagerConfiguration {
  private File officeHome = OfficeEnvironment.getDefaultOffice();
  /** 本地连接协议 */
  private LocalConnectionProtocol connectionProtocol = LocalConnectionProtocol.SOCKET;
  /** 端口号 */
  private int[] portNumbers = new int[] {2002};
  /** 管道名字 */
  private String[] pipeNames = new String[] {"office"};
  /** 运行参数 */
  private String[] runAsArgs = null;
  /** 模板属性目录 */
  private String templateProfileDir = null;
  /** 工作目录 */
  private String workDir = System.getProperty("java.io.tmpdir");
  /** 任务延迟 */
  private long taskQueueTimeout = 30000L;
  /** 任务执行延迟 */
  private long taskExecutionTimeout = 120000L;
  /** 最大任务每个 */
  private int maxTasksPerProcess = 200;
  /** 重新尝试延迟 */
  private long retryTimeout = 120000L;

  private ProcessManager processManager = null;

  public OfficeManager buildOfficeManager() throws IllegalStateException {
    if (templateProfileDir != null) {
      File templateProfileDirFile = new File(templateProfileDir, "user");
      if (!templateProfileDirFile.isDirectory()) {
        throw new IllegalStateException(
            "templateProfileDir doesn't appear to contain a user profile: " + templateProfileDir);
      }
    }
    File workDirFile = new File(workDir);
    if (!workDirFile.isDirectory()) {
      throw new IllegalStateException("workDir doesn't exist or is not a directory: " + workDir);
    }

    if (processManager == null) {
      processManager = findBestProcessManager();
    }

    int numInstances =
        connectionProtocol == LocalConnectionProtocol.PIPE ? pipeNames.length : portNumbers.length;
    UnoUrl[] unoUrls = new UnoUrl[numInstances];
    for (int i = 0; i < numInstances; i++) {
      unoUrls[i] =
          (connectionProtocol == LocalConnectionProtocol.PIPE)
              ? UnoUrl.pipe(pipeNames[i])
              : UnoUrl.socket(portNumbers[i]);
    }
    return new ProcessPoolOfficeManager(
        officeHome,
        unoUrls,
        runAsArgs,
        templateProfileDir,
        workDir,
        retryTimeout,
        taskQueueTimeout,
        taskExecutionTimeout,
        maxTasksPerProcess,
        processManager);
  }

  private ProcessManager findBestProcessManager() {
    if (Is.linuxSystem()) {
      return ProcessManager.ofLinux(runAsArgs);
    } else {
      // NOTE: UnixProcessManager can't be trusted to work on Solaris
      // because of the 80-char limit on ps output there
      return new PureJavaProcessManager();
    }
  }

  // --------------------------------------------------------------------------- get set

  public File getOfficeHome() {
    return officeHome;
  }

  public void setOfficeHome(File officeHome) {
    this.officeHome = officeHome;
  }

  public LocalConnectionProtocol getConnectionProtocol() {
    return connectionProtocol;
  }

  public void setConnectionProtocol(LocalConnectionProtocol connectionProtocol) {
    this.connectionProtocol = connectionProtocol;
  }

  public int[] getPortNumbers() {
    return portNumbers;
  }

  public void setPortNumbers(int... portNumbers) {
    this.portNumbers = portNumbers;
  }

  public String[] getPipeNames() {
    return pipeNames;
  }

  public void setPipeNames(String[] pipeNames) {
    this.pipeNames = pipeNames;
  }

  public String[] getRunAsArgs() {
    return runAsArgs;
  }

  public void setRunAsArgs(String[] runAsArgs) {
    this.runAsArgs = runAsArgs;
  }

  public String getTemplateProfileDir() {
    return templateProfileDir;
  }

  public void setTemplateProfileDir(String templateProfileDir) {
    this.templateProfileDir = templateProfileDir;
  }

  public String getWorkDir() {
    return workDir;
  }

  public void setWorkDir(String workDir) {
    this.workDir = workDir;
  }

  public long getTaskQueueTimeout() {
    return taskQueueTimeout;
  }

  public void setTaskQueueTimeout(long taskQueueTimeout) {
    this.taskQueueTimeout = taskQueueTimeout;
  }

  public long getTaskExecutionTimeout() {
    return taskExecutionTimeout;
  }

  public void setTaskExecutionTimeout(long taskExecutionTimeout) {
    this.taskExecutionTimeout = taskExecutionTimeout;
  }

  public int getMaxTasksPerProcess() {
    return maxTasksPerProcess;
  }

  public void setMaxTasksPerProcess(int maxTasksPerProcess) {
    this.maxTasksPerProcess = maxTasksPerProcess;
  }

  public long getRetryTimeout() {
    return retryTimeout;
  }

  public void setRetryTimeout(long retryTimeout) {
    this.retryTimeout = retryTimeout;
  }

  public ProcessManager getProcessManager() {
    return processManager;
  }

  public void setProcessManager(ProcessManager processManager) {
    this.processManager = processManager;
  }
}
