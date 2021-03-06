package pxf.infrastructure.system.template;

import pxf.infrastructure.system.basic.dbentity.QueryModel;

/**
 * @author potatoxf
 * @date 2021/5/13
 */
public class TemplateQueryModel extends QueryModel {

  /**
   * 模板唯一识别码
   *
   * <p>{@code catalog},{@code group},{@code name}三者唯一
   */
  private String code;
  /** 分类 */
  private String catalog;
  /** 组 */
  private String group;
  /** 名称 */
  private String name;
  /** 类型 */
  private Integer type;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCatalog() {
    return catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }
}
