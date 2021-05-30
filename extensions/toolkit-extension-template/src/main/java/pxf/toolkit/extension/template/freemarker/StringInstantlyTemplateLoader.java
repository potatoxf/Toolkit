package pxf.toolkit.extension.template.freemarker;

import freemarker.cache.TemplateLoader;
import java.io.Reader;
import java.io.StringReader;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Freemarker 字符串模板加载器
 *
 * @author potatoxf
 * @date 2021/4/19
 */
@ThreadSafe
public class StringInstantlyTemplateLoader implements TemplateLoader {

  private final ThreadLocal<String> templateThreadLocal = new ThreadLocal<>();

  public void init(String template) {
    templateThreadLocal.set(template);
  }

  @Override
  public void closeTemplateSource(Object templateSource) {}

  @Override
  public Object findTemplateSource(String name) {
    String template = templateThreadLocal.get();
    if (name != null && template != null) {
      if (name.length() == template.length()) {
        if (name.equals(template)) {
          templateThreadLocal.remove();
          return name;
        }
      }
    }
    return null;
  }

  @Override
  public long getLastModified(Object templateSource) {
    return 0;
  }

  @Override
  public Reader getReader(Object templateSource, String encoding) {
    return new StringReader((String) templateSource);
  }
}
