package pxf.extsweb.fileonlinepreview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

  /** 访问外部文件配置 */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String filePath = ConfigConstants.getFileDir();
    LOGGER.info("Add resource locations: {}", filePath);
    registry
        .addResourceHandler("/**")
        .addResourceLocations(
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/",
            "file:" + filePath);
  }
}
