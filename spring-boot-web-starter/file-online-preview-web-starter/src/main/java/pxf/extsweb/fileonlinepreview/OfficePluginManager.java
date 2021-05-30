package pxf.extsweb.fileonlinepreview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pxf.toolkit.extension.office.OfficeDocumentConverter;
import pxf.toolkit.extension.office.OfficeEnvironment;
import pxf.toolkit.extension.office.manager.OfficeManager;
import pxf.toolkit.extension.office.manager.OfficeManagerConfiguration;

/** 创建文件转换器 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OfficePluginManager {

  private final Logger logger = LoggerFactory.getLogger(OfficePluginManager.class);

  private OfficeManager officeManager;

  @PostConstruct
  public void initOfficeManager() {
    new Thread(this::startOfficeManager).start();
  }

  /** 启动Office组件进程 */
  private void startOfficeManager() {
    File officeHome = OfficeEnvironment.getDefaultOffice();
    if (officeHome == null) {
      throw new RuntimeException("找不到office组件，请确认'office.home'配置是否有误");
    }
    boolean killOffice = killProcess();
    if (killOffice) {
      logger.warn("检测到有正在运行的office进程，已自动结束该进程");
    }
    try {
      OfficeManagerConfiguration configuration = new OfficeManagerConfiguration();
      configuration.setOfficeHome(officeHome);
      configuration.setPortNumbers(8100);
      // 设置任务执行超时为5分钟
      configuration.setTaskExecutionTimeout(1000 * 60 * 5L);
      // 设置任务队列超时为24小时
      // configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
      officeManager = configuration.buildOfficeManager();
      officeManager.start();
    } catch (Exception e) {
      logger.error("启动office组件失败，请检查office组件是否可用");
      throw e;
    }
  }

  public OfficeDocumentConverter getDocumentConverter() {
    OfficeDocumentConverter converter =
        new OfficeDocumentConverter(officeManager, new OfficePluginExtendFormatRegistry());
    converter.setDefaultLoadProperties(getLoadProperties());
    return converter;
  }

  private Map<String, ?> getLoadProperties() {
    Map<String, Object> loadProperties = new HashMap<>(10);
    loadProperties.put("Hidden", true);
    loadProperties.put("ReadOnly", true);
    loadProperties.put("UpdateDocMode", "UpdateDocMode.QUIET_UPDATE");
    loadProperties.put("CharacterSet", StandardCharsets.UTF_8.name());
    return loadProperties;
  }

  private boolean killProcess() {
    boolean flag = false;
    Properties props = System.getProperties();
    try {
      if (props.getProperty("os.name").toLowerCase().contains("windows")) {
        Process p = Runtime.getRuntime().exec("cmd /c tasklist ");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream os = p.getInputStream();
        byte[] b = new byte[256];
        while (os.read(b) > 0) {
          baos.write(b);
        }
        String s = baos.toString();
        if (s.contains("soffice.bin")) {
          Runtime.getRuntime().exec("taskkill /im " + "soffice.bin" + " /f");
          flag = true;
        }
      } else {
        Process p =
            Runtime.getRuntime().exec(new String[] {"sh", "-c", "ps -ef | grep " + "soffice.bin"});
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream os = p.getInputStream();
        byte[] b = new byte[256];
        while (os.read(b) > 0) {
          baos.write(b);
        }
        String s = baos.toString();
        if (StringUtils.ordinalIndexOf(s, "soffice.bin", 3) > 0) {
          String[] cmd = {
            "sh", "-c", "kill -15 `ps -ef|grep " + "soffice.bin" + "|awk 'NR==1{print $2}'`"
          };
          Runtime.getRuntime().exec(cmd);
          flag = true;
        }
      }
    } catch (IOException e) {
      logger.error("检测office进程异常", e);
    }
    return flag;
  }

  @PreDestroy
  public void destroyOfficeManager() {
    if (null != officeManager && officeManager.isRunning()) {
      officeManager.stop();
    }
  }
}
