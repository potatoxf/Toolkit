package pxf.toolkit.extension.office.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.exception.RetryException;
import pxf.toolkit.basic.function.Retryable;
import pxf.toolkit.basic.util.Cast;
import pxf.toolkit.basic.util.Is;
import pxf.toolkit.basic.util.Valid;
import pxf.toolkit.extension.office.OfficeEnvironment;
import pxf.toolkit.extension.system.process.ProcessManager;

/**
 * @author potatoxf
 * @date 2021/4/20
 */
class OfficeProcess implements Retryable {

  private static final Logger LOG = LoggerFactory.getLogger(OfficeProcess.class);
  private final File officeHome;
  private final UnoUrl unoUrl;
  private final String[] runAsArgs;
  private final File templateProfileDir;
  private final File instanceProfileDir;
  private final ProcessManager processManager;
  private Process process;
  private long pid = ProcessManager.PID_UNKNOWN;
  private int exitCode;

  public OfficeProcess(
      File officeHome,
      UnoUrl unoUrl,
      String[] runAsArgs,
      String templateProfileDir,
      String workDir,
      ProcessManager processManager) {
    this.officeHome = officeHome;
    this.unoUrl = unoUrl;
    this.runAsArgs = runAsArgs;
    this.templateProfileDir = Valid.file(templateProfileDir);
    this.instanceProfileDir = getInstanceProfileDir(Valid.file(workDir), unoUrl);
    this.processManager = processManager;
  }

  public void start() throws Throwable {
    start(false);
  }

  public void start(boolean restart) throws Throwable {
    long existingPid = processManager.findProcessId("soffice.bin", unoUrl.getAcceptString());
    if (!(existingPid == ProcessManager.PID_NOT_FOUND
        || existingPid == ProcessManager.PID_UNKNOWN)) {
      throw new IllegalStateException(
          String.format(
              "a process with acceptString '%s' is already running; pid %d",
              unoUrl.getAcceptString(), existingPid));
    }
    if (!restart) {
      prepareInstanceProfileDir();
    }
    List<String> command = new ArrayList<>();
    File executable = OfficeEnvironment.getOfficeExecutable(officeHome);
    if (runAsArgs != null) {
      command.addAll(Arrays.asList(runAsArgs));
    }
    command.add(executable.getAbsolutePath());
    command.add("-accept=" + unoUrl.getAcceptString() + ";urp;");
    command.add("-env:UserInstallation=" + Cast.urlFromFile(instanceProfileDir));
    command.add("-headless");
    command.add("-nocrashreport");
    command.add("-nodefault");
    command.add("-nofirststartwizard");
    command.add("-nolockcheck");
    command.add("-nologo");
    command.add("-norestore");
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    if (Is.windowsSystem()) {
      addBasisAndUrePaths(processBuilder);
    }
    if (LOG.isInfoEnabled()) {
      LOG.info(
          String.format(
              "starting process with acceptString '%s' and profileDir '%s'",
              unoUrl, instanceProfileDir));
    }
    process = processBuilder.start();
    pid = processManager.findProcessId("soffice.bin", unoUrl.getAcceptString());
    if (pid == ProcessManager.PID_NOT_FOUND) {
      throw new IllegalStateException(
          String.format(
              "process with acceptString '%s' started but its pid could not be found",
              unoUrl.getAcceptString()));
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("started process" + (pid != ProcessManager.PID_UNKNOWN ? "; pid = " + pid : ""));
    }
  }

  private File getInstanceProfileDir(File workDir, UnoUrl unoUrl) {
    String dirName =
        ".jodconverter_" + unoUrl.getAcceptString().replace(',', '_').replace('=', '-');
    return new File(workDir, dirName);
  }

  private void prepareInstanceProfileDir() throws OfficeException {
    if (instanceProfileDir.exists()) {
      if (LOG.isWarnEnabled()) {
        LOG.warn(String.format("profile dir '%s' already exists; deleting", instanceProfileDir));
      }
      deleteProfileDir();
    }
    if (templateProfileDir != null) {
      try {
        FileUtils.copyDirectory(templateProfileDir, instanceProfileDir);
      } catch (IOException ioException) {
        throw new OfficeException("failed to create profileDir", ioException);
      }
    }
  }

  public void deleteProfileDir() {
    if (instanceProfileDir != null) {
      try {
        FileUtils.deleteDirectory(instanceProfileDir);
      } catch (IOException ioException) {
        File oldProfileDir =
            new File(
                instanceProfileDir.getParentFile(),
                instanceProfileDir.getName() + ".old." + System.currentTimeMillis());
        if (instanceProfileDir.renameTo(oldProfileDir)) {
          if (LOG.isWarnEnabled()) {
            LOG.warn(
                "could not delete profileDir: "
                    + ioException.getMessage()
                    + "; renamed it to "
                    + oldProfileDir);
          }
        } else {
          if (LOG.isErrorEnabled()) {
            LOG.error("could not delete profileDir: " + ioException.getMessage());
          }
        }
      }
    }
  }

  private void addBasisAndUrePaths(ProcessBuilder processBuilder) throws IOException {
    // see http://wiki.services.openoffice.org/wiki/ODF_Toolkit/Efforts/Three-Layer_OOo
    File basisLink = new File(officeHome, "basis-link");
    if (!basisLink.isFile()) {
      if (LOG.isWarnEnabled()) {
        LOG.warn(
            "no %OFFICE_HOME%/basis-link found; assuming it's OOo 2.x and we don't need to append URE and Basic paths");
      }
      return;
    }
    String basisLinkText = FileUtils.readFileToString(basisLink, StandardCharsets.UTF_8).trim();
    File basisHome = new File(officeHome, basisLinkText);
    File basisProgram = new File(basisHome, "program");
    File ureLink = new File(basisHome, "ure-link");
    String ureLinkText = FileUtils.readFileToString(ureLink, StandardCharsets.UTF_8).trim();
    File ureHome = new File(basisHome, ureLinkText);
    File ureBin = new File(ureHome, "bin");
    Map<String, String> environment = processBuilder.environment();
    // Windows environment variables are case insensitive but Java maps are not :-/
    // so let's make sure we modify the existing key
    String pathKey = "PATH";
    for (String key : environment.keySet()) {
      if ("PATH".equalsIgnoreCase(key)) {
        pathKey = key;
      }
    }
    String path =
        environment.get(pathKey)
            + ";"
            + ureBin.getAbsolutePath()
            + ";"
            + basisProgram.getAbsolutePath();
    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("setting %s to %s", pathKey, path));
    }
    environment.put(pathKey, path);
  }

  public boolean isRunning() {
    if (process == null) {
      return false;
    }
    return getExitCode() == null;
  }

  public Integer getExitCode() {
    try {
      return process.exitValue();
    } catch (IllegalThreadStateException exception) {
      return null;
    }
  }

  public int getExitCode(long retryInterval, long retryTimeout) throws RetryException {
    execute(retryInterval, retryTimeout);
    return exitCode;
  }

  public int forciblyTerminate(long retryInterval, long retryTimeout) throws Throwable {
    if (LOG.isInfoEnabled()) {
      LOG.info(
          String.format(
              "trying to forcibly terminate process: '%s' %s",
              unoUrl, (pid != ProcessManager.PID_UNKNOWN ? " (pid " + pid + ")" : "")));
    }
    processManager.killProcess(process, pid);
    return getExitCode(retryInterval, retryTimeout);
  }

  @Override
  public void attempt() {
    exitCode = process.exitValue();
  }
}
