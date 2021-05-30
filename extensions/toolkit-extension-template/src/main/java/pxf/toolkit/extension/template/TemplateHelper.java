package pxf.toolkit.extension.template;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.exception.ParseException;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.extension.template.freemarker.FreemarkerStringTemplateResolver;

/**
 * 模板助手类
 *
 * @author potatoxf
 * @date 2021/4/19
 */
public class TemplateHelper {

  private static final Logger LOG = LoggerFactory.getLogger(TemplateHelper.class);
  /** 默认文本模板解析 */
  private static final FreemarkerStringTemplateResolver DEFAULT_TEXT_TEMPLATE_RESOLVER =
      new FreemarkerStringTemplateResolver();

  private TemplateHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 处理字符串模板解析
   *
   * <p>暂时只支持 Freemarker
   *
   * @param template 模板
   * @param container 容器
   * @return 处理后的字符串
   */
  @SuppressWarnings("rawtypes")
  public static String parseTemplateSilently(String template, Map container) {
    try {
      return TemplateHelper.parseTemplate(template, container);
    } catch (ParseException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("Template parsing error", e);
      }
    }
    return Empty.STRING;
  }

  /**
   * 处理字符串模板解析
   *
   * <p>暂时只支持 Freemarker
   *
   * @param template 模板
   * @param container 容器
   * @return 处理后的字符串
   * @throws ParseException 解析异常
   */
  @SuppressWarnings("rawtypes")
  public static String parseTemplate(String template, Map container) throws ParseException {
    return DEFAULT_TEXT_TEMPLATE_RESOLVER.handle(template, container);
  }
}
