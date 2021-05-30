package pxf.toolkit.extension.office;

import com.sun.star.beans.PropertyValue;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.uno.UnoRuntime;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.exception.IOFlowException;
import pxf.toolkit.basic.lang.AbstractEnvironmentPath;
import pxf.toolkit.basic.util.Is;
import pxf.toolkit.basic.util.Legalize;
import pxf.toolkit.basic.util.Read;
import pxf.toolkit.extension.office.format.DocumentFamily;
import pxf.toolkit.extension.office.manager.OfficeException;

/**
 * Office环境
 *
 * @author potatoxf
 * @date 2021/4/18
 */
@SuppressWarnings("unchecked")
public final class OfficeEnvironment extends AbstractEnvironmentPath {

  /** xxx/office/ */
  public static final String OFFICE_HOME = "OFFICE_HOME";
  /** xxx/office/office-plugin/ */
  public static final String OFFICE_PLUGIN = "OFFICE_PLUGIN";
  /** xxx/office/office-config.properties */
  public static final String OFFICE_CUSTOMIZED_CONFIG = "CUSTOMIZED_CONFIG";

  public static final String SERVICE_DESKTOP = "com.sun.star.frame.Desktop";
  public static final String OFFICE_HOME_KEY = "office.home";
  public static final String DEFAULT_OFFICE_HOME_VALUE = "default";
  private static final Logger LOG = LoggerFactory.getLogger(OfficeEnvironment.class);
  /** Mac查找路径 */
  private static final String[] MAC_FOUND_PATH = {
    "/Applications/OpenOffice.org.app/Contents",
    "/Applications/OpenOffice.app/Contents",
    "/Applications/LibreOffice.app/Contents"
  };
  /** Linux查找路径 */
  private static final String[] LINUX_FOUND_PATH = {
    "/opt/openoffice.org3", "/opt/openoffice",
    "/opt/libreoffice", "/opt/openoffice4",
    "/usr/lib/openoffice", "/usr/lib/libreoffice"
  };

  /** 单例 */
  private static OfficeEnvironment INSTANCE;

  /** 配置 */
  private final Properties config = new Properties();

  private OfficeEnvironment() {}

  private OfficeEnvironment(String environmentKey, String propertyKey) {
    super(environmentKey, propertyKey);
    configPathDecorator(new OfficeHomePath(), new OfficePluginPath(), new CustomizedConfigPath());
  }

  public static OfficeEnvironment getInstance() {
    if (INSTANCE == null) {
      synchronized (OfficeEnvironment.class) {
        if (INSTANCE == null) {
          INSTANCE = new OfficeEnvironment();
        }
      }
    }
    return INSTANCE;
  }

  @Nonnull
  public static File getDefaultOffice() {
    return getInstance().getOfficeHome();
  }

  public static PropertyValue[] toUnoProperties(Map<String, ?> properties) {
    PropertyValue[] propertyValues = new PropertyValue[properties.size()];
    int i = 0;
    for (Map.Entry<String, ?> entry : properties.entrySet()) {
      Object value = entry.getValue();
      if (value instanceof Map) {
        Map<String, Object> subProperties = (Map<String, Object>) value;
        value = toUnoProperties(subProperties);
      }
      PropertyValue propertyValue = new PropertyValue();
      propertyValue.Name = entry.getKey();
      propertyValue.Value = value;
      propertyValues[i++] = propertyValue;
    }
    return propertyValues;
  }

  public static DocumentFamily supportDocumentFamily(XComponent document) throws OfficeException {
    XServiceInfo serviceInfo = UnoRuntime.queryInterface(XServiceInfo.class, document);
    if (serviceInfo.supportsService("com.sun.star.text.GenericTextDocument")) {
      // NOTE: a GenericTextDocument is either a TextDocument, a WebDocument, or a GlobalDocument
      // but this further distinction doesn't seem to matter for conversions
      return DocumentFamily.TEXT;
    } else if (serviceInfo.supportsService("com.sun.star.sheet.SpreadsheetDocument")) {
      return DocumentFamily.SPREADSHEET;
    } else if (serviceInfo.supportsService("com.sun.star.presentation.PresentationDocument")) {
      return DocumentFamily.PRESENTATION;
    } else if (serviceInfo.supportsService("com.sun.star.drawing.DrawingDocument")) {
      return DocumentFamily.DRAWING;
    } else {
      throw new OfficeException(
          "document of unknown family: " + serviceInfo.getImplementationName());
    }
  }

  /**
   * 是否是Office可执行文件
   *
   * @param officeHome office主目录
   * @return {@code File}
   */
  @Nonnull
  public static File getOfficeExecutable(String officeHome) {
    File file;
    if (Is.macSystem()) {
      file = new File(officeHome, "MacOS/soffice");
    } else {
      file = new File(officeHome, "program/soffice.bin");
    }
    return file;
  }

  /**
   * 是否是Office可执行文件
   *
   * @param officeHome office主目录
   * @return {@code File}
   */
  @Nonnull
  public static File getOfficeExecutable(File officeHome) {
    File file;
    if (Is.macSystem()) {
      file = new File(officeHome, "MacOS/soffice");
    } else {
      file = new File(officeHome, "program/soffice.bin");
    }
    return file;
  }

  /**
   * 是否是Office可执行主目录
   *
   * @param officeHome office主目录
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  public static boolean isOfficeExecutableHome(String officeHome) {
    return getOfficeExecutable(officeHome).isFile();
  }

  /**
   * 查找Office家目录
   *
   * @param knownPaths 已知子路径
   * @return {@code File}
   */
  @Nonnull
  private static File findOfficeHome(String... knownPaths) {
    List<File> files = new ArrayList<>(knownPaths.length);
    for (String path : knownPaths) {
      if (isOfficeExecutableHome(path)) {
        files.add(new File(path));
      }
    }
    throw new IOFlowException("The configuration is not found in the office home for " + files);
  }

  /**
   * xxx/office/
   *
   * @return {@code String}
   */
  public String getOfficeEnvPath() {
    return getDecoratedConfigPathString(OFFICE_HOME);
  }

  /**
   * xxx/office/office-plugin
   *
   * @return {@code String}
   */
  public String getOfficePluginPath() {
    return getDecoratedConfigPathString(OFFICE_PLUGIN);
  }

  /**
   * xxx/office/office-config.properties
   *
   * @return {@code String}
   */
  public String getOfficeCustomizedConfigPath() {
    return getDecoratedConfigPathString(OFFICE_CUSTOMIZED_CONFIG);
  }

  /**
   * 获取的Office安装位置的主要目录位置
   *
   * @return {@code File}，当没有找到时返回 {@code null}
   */
  @Nonnull
  public File getOfficeHome() {
    loadConfig();
    String officeHome = config.getProperty(OFFICE_HOME_KEY);
    if (officeHome != null && !DEFAULT_OFFICE_HOME_VALUE.equals(officeHome)) {
      File officeHomeDir = new File(officeHome);
      if (!officeHomeDir.isDirectory()) {
        throw new IllegalStateException("The office home isn't directory for " + officeHome);
      } else if (!isOfficeExecutableHome(officeHome)) {
        throw new IllegalStateException("The directory isn't office home for " + officeHome);
      }
      return officeHomeDir;
    }
    if (Is.windowsSystem()) {
      // %ProgramFiles(x86)% on 64-bit machines; %ProgramFiles% on 32-bit ones
      String officePluginPath = getOfficePluginPath();
      String programFiles = System.getenv("ProgramFiles(x86)");
      if (programFiles == null) {
        programFiles = System.getenv("ProgramFiles");
      }
      programFiles = Legalize.directoryPath(programFiles);
      return findOfficeHome(
          programFiles + "OpenOffice 4",
          programFiles + "LibreOffice 4",
          officePluginPath + "windows-office");
    } else if (Is.macSystem()) {
      return findOfficeHome(MAC_FOUND_PATH);
    } else {
      // Linux or other *nix variants
      return findOfficeHome(LINUX_FOUND_PATH);
    }
  }

  /**
   * 加载配置并返回一个副本
   *
   * @return {@code Properties}
   */
  @Nonnull
  public Properties loadConfig() {
    try {
      config.clear();
      String customizedConfigPath = getOfficeCustomizedConfigPath();
      Properties properties = Read.propertiesFromFile(customizedConfigPath);
      config.putAll(properties);
      return properties;
    } catch (IOException e) {
      throw new IOFlowException("Error loading configuration", e);
    }
  }

  private static class OfficeHomePath implements PathDecorator {

    @Override
    public Path apply(Path path) {
      return path.resolve("office");
    }

    @Override
    public Object category() {
      return OFFICE_HOME;
    }
  }

  private static class OfficePluginPath implements PathDecorator {

    @Override
    public Path apply(Path path) {
      return path.resolve("office").resolve("office-plugin");
    }

    @Override
    public Object category() {
      return OFFICE_PLUGIN;
    }
  }

  private static class CustomizedConfigPath implements PathDecorator {

    @Override
    public Path apply(Path path) {
      return path.resolve("office").resolve("office-config.properties");
    }

    @Override
    public Object category() {
      return OFFICE_CUSTOMIZED_CONFIG;
    }
  }
}
