package pxf.toolkit.extension.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import pxf.toolkit.basic.exception.ParseException;
import pxf.toolkit.extension.template.StringTemplateResolver;

/**
 * Freemarker 字符串模板解析器
 *
 * @author potatoxf
 * @date 2021/4/19
 */
public class FreemarkerStringTemplateResolver implements StringTemplateResolver {

  private final Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
  private final StringInstantlyTemplateLoader stringInstantlyTemplateLoader =
      new StringInstantlyTemplateLoader();

  public FreemarkerStringTemplateResolver() {
    this(StandardCharsets.UTF_8);
  }

  public FreemarkerStringTemplateResolver(Charset charset) {
    configuration.setTemplateLoader(stringInstantlyTemplateLoader);
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setDefaultEncoding(charset.name());
  }

  /**
   * 处理字符串模板解析
   *
   * <p>主要用于字符串变量替换，具体语法参见解析器的支持情况
   *
   * @param template 模板
   * @param container 容器
   * @return 处理后的字符串
   */
  @Override
  @SuppressWarnings("rawtypes")
  public String handle(String template, Map container) throws ParseException {
    try {
      stringInstantlyTemplateLoader.init(template);
      StringWriter writer = new StringWriter();
      configuration.getTemplate(template).process(container, writer);
      return writer.toString();
    } catch (TemplateException | IOException e) {
      throw new ParseException(e);
    }
  }
}
