package pxf.infrastructure.system.setting.entity;

import java.util.Objects;

/**
 * 系统设置实体索引
 *
 * @author potatoxf
 * @date 2021/5/30
 */
public class SystemSettingIndex {

  /** 模块名 */
  private String module;
  /** 类名 */
  private String clazz;
  /** 分类 */
  private String catalog;
  /** 名称 */
  private String name;

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public String getCatalog() {
    return catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemSettingIndex that = (SystemSettingIndex) o;
    return Objects.equals(getModule(), that.getModule())
        && Objects.equals(getClazz(), that.getClazz())
        && Objects.equals(getCatalog(), that.getCatalog())
        && Objects.equals(getName(), that.getName());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getModule(), getClazz(), getCatalog(), getName());
  }

  @Override
  public final String toString() {
    return getClass().getName() + "[" + getCatalog() + " : " + getName() + "]";
  }
}
