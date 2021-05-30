package pxf.springboot.starter.setting;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pxf.infrastructure.system.setting.SystemSettingConfig;
import pxf.infrastructure.system.setting.SystemSettingManagerService;
import pxf.infrastructure.system.setting.service.SystemSettingService;
import pxf.infrastructure.system.setting.service.SystemSettingServiceImpl;

/**
 * @author potatoxf
 * @date 2021/5/29
 */
@Configuration
@MapperScan("pxf.infrastructure.system.setting.mapper")
public class SettingManagerCenter implements CommandLineRunner {
  private static final Logger LOG = LoggerFactory.getLogger(SettingManagerCenter.class);
  @Autowired private SystemSettingManagerService systemSettingManagerService;

  @Bean
  public SystemSettingService systemSettingService() {
    return new SystemSettingServiceImpl();
  }

  @Bean
  @ConfigurationProperties(prefix = "system.setting")
  public SystemSettingConfig systemSettingConfig() {
    return new SystemSettingConfig();
  }

  @Bean
  public SystemSettingManagerService systemSettingManagerService(
      SystemSettingService systemSettingService, SystemSettingConfig systemSettingConfig) {
    return new SystemSettingManagerService(systemSettingService, systemSettingConfig);
  }

  @Override
  public void run(String... args) {
    if (LOG.isInfoEnabled()) {
      LOG.info("====>> Start checking system configuration");
    }
    systemSettingManagerService.refresh();
    if (LOG.isInfoEnabled()) {
      LOG.info("====<< Complete the detection system configuration");
    }
  }
}
