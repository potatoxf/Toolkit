package pxf.infrastructure.system.template;

/**
 * @author potatoxf
 * @date 2021/5/13
 */
public interface SystemTemplateManger {

  /**
   * 移除模板
   *
   */
  boolean searchTemplate(TemplateQueryModel templateQueryModel);
  /**
   * 移除模板
   *
   */
  boolean addTemplate(TemplateDataModel templateContext);
  /**
   * 移除模板
   *
   * @param id ID
   */
  boolean removeTemplate(Long id);
}
