package pxf.infrastructure.system.template;

import freemarker.cache.TemplateLoader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.function.Supplier;
import pxf.infrastructure.system.template.entity.SystemTemplate;
import pxf.infrastructure.system.template.entity.SystemTemplateContent;
import pxf.infrastructure.system.template.service.SystemTemplateContentService;
import pxf.infrastructure.system.template.service.SystemTemplateService;
import pxf.toolkit.basic.lang.ImmutablePair;
import pxf.toolkit.basic.lang.Pair;

/**
 * 数据库模板加载器
 *
 * @author potatoxf
 * @date 2021/5/12
 */
@SuppressWarnings("unchecked")
public class DatabaseTemplateLoader implements TemplateLoader {

  private final SystemTemplateService systemTemplateService;
  private final SystemTemplateContentService systemTemplateContentService;

  public DatabaseTemplateLoader(
      SystemTemplateService systemTemplateService,
      SystemTemplateContentService systemTemplateContentService) {
    this.systemTemplateService = systemTemplateService;
    this.systemTemplateContentService = systemTemplateContentService;
  }

  @Override
  public Pair<Long, Supplier<Clob>> findTemplateSource(String code) throws IOException {
    if (!systemTemplateService.isExistTemplate(code)) {
      return null;
    }
    SystemTemplate systemTemplate = systemTemplateService.searchByCode(code);
    if (systemTemplate == null) {
      return null;
    }
    Long contextId = systemTemplate.getContextId();
    if (contextId == null) {
      return null;
    }
    return new ImmutablePair<>(
        contextId,
        () -> {
          SystemTemplateContent systemTemplateContent =
              systemTemplateContentService.getById(contextId);
          if (systemTemplateContent != null) {
            return systemTemplateContent.getContent();
          }
          return null;
        });
  }

  @Override
  public long getLastModified(Object templateSource) {
    if (templateSource == null) {
      return 0;
    }
    Long contextId = ((Pair<Long, Supplier<Clob>>) templateSource).getKey();
    return systemTemplateContentService.getLastUpdateTimestampById(contextId);
  }

  @Override
  public Reader getReader(Object templateSource, String encoding) throws IOException {
    if (templateSource == null) {
      return null;
    }
    Supplier<Clob> clobSupplier = ((Pair<Long, Supplier<Clob>>) templateSource).getValue();
    try {
      return clobSupplier.get().getCharacterStream();
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void closeTemplateSource(Object o) throws IOException {}
}
