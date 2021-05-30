package pxf.infrastructure.system.template.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import pxf.infrastructure.system.basic.dbentity.DataConfigEntity;

/**
 * 系统模板
 *
 * @author potatoxf
 * @date 2021/5/12
 */
@TableName("pxf_sys_template")
public class SystemTemplate extends DataConfigEntity<SystemTemplate, Long> {

  /** 默认分类 */
  public static final String DEFAULT_CATALOG = "DC";
  /** 默认组 */
  public static final String DEFAULT_GROUP = "DG";
  /**
   * 模板唯一识别码
   *
   * <p>{@code catalog},{@code group},{@code name}三者唯一
   */
  private String code;
  /** 分类 */
  private String catalog = DEFAULT_CATALOG;
  /** 组 */
  private String group = DEFAULT_GROUP;
  /** 名称 */
  private String name;
  /** 类型 */
  private Integer type;
  /** 模板数据 */
  private Long contextId;

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

  public Long getContextId() {
    return contextId;
  }

  public void setContextId(Long contextId) {
    this.contextId = contextId;
  }
}
