package pxf.infrastructure.system.setting.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Objects;
import pxf.infrastructure.system.basic.dbentity.DataConfigEntity;
import pxf.infrastructure.system.setting.SystemSettingConstant;
import pxf.toolkit.basic.util.Compare;

/**
 * 系统设置实体
 *
 * @author potatoxf
 * @date 2021/4/23
 */
@TableName("pxf_sys_setting")
public class SystemSetting extends DataConfigEntity<SystemSetting, Long> {

  /** 模块名 */
  @TableField(
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String module;
  /** 类名 */
  @TableField(
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String clazz;
  /** 分类 */
  @TableField(
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String catalog;
  /** 名称 */
  @TableField(
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String name;
  /** 类型 */
  @TableField(
      condition = SqlCondition.EQUAL,
      insertStrategy = FieldStrategy.NOT_NULL,
      updateStrategy = FieldStrategy.NOT_NULL)
  private Integer type;
  /** 值 */
  private String value;

  public static SystemSetting from(SystemSettingConstant systemSettingConstant) {
    SystemSetting systemSetting = new SystemSetting();
    systemSetting.setModule(systemSettingConstant.getModule());
    systemSetting.setClazz(systemSettingConstant.getClazz());
    systemSetting.setCatalog(systemSettingConstant.getCatalog());
    systemSetting.setName(systemSettingConstant.getName());
    systemSetting.setType(systemSettingConstant.getSettingType().identity());
    systemSetting.setComment(systemSettingConstant.comment());
    Object defaultValue = systemSettingConstant.getDefaultValue();
    if (defaultValue != null) {
      systemSetting.setValue(defaultValue.toString());
    }
    return systemSetting;
  }

  public SystemSettingIndex toIndex() {
    SystemSettingIndex index = new SystemSettingIndex();
    index.setModule(getModule());
    index.setClazz(getClazz());
    index.setCatalog(getCatalog());
    index.setName(getName());
    return index;
  }

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

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int compareTo(SystemSetting other) {
    int cp = Compare.objectOrComparableForPriorityNull(getModule(), other.getModule());
    if (cp == 0) {
      cp = Compare.objectOrComparableForPriorityNull(getClazz(), other.getClazz());
      if (cp == 0) {
        cp = Compare.objectOrComparableForPriorityNull(getCatalog(), other.getCatalog());
        if (cp == 0) {
          cp = Compare.objectOrComparableForPriorityNull(getName(), other.getName());
        }
      }
    }
    return cp;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemSetting that = (SystemSetting) o;
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

  public static class Query extends DataConfigEntity.Query {
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

    public Query setModule(String module) {
      this.module = module;
      return this;
    }

    public String getClazz() {
      return clazz;
    }

    public Query setClazz(String clazz) {
      this.clazz = clazz;
      return this;
    }

    public String getCatalog() {
      return catalog;
    }

    public Query setCatalog(String catalog) {
      this.catalog = catalog;
      return this;
    }

    public String getName() {
      return name;
    }

    public Query setName(String name) {
      this.name = name;
      return this;
    }
  }
}
