package pxf.infrastructure.system.basic.dbentity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import pxf.infrastructure.system.basic.constant.EntityStatus;
import pxf.toolkit.basic.function.Cleanable;
import pxf.toolkit.basic.util.Const;
import pxf.toolkit.basic.util.Copy;
import pxf.toolkit.basic.util.New;

/**
 * 实体接口
 *
 * @author potatoxf
 * @date 2021/5/13
 */
public abstract class Entity implements Cloneable, Serializable, Cleanable {

  /** 状态 */
  @TableField(
      fill = FieldFill.INSERT,
      condition = SqlCondition.EQUAL,
      insertStrategy = FieldStrategy.NOT_NULL,
      updateStrategy = FieldStrategy.NOT_NULL)
  private Integer status;

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public EntityStatus getEntityStatus() {
    return Const.parseFindableConstant(EntityStatus.class, status);
  }

  public void setEntityStatus(EntityStatus entityStatus) {
    this.status = entityStatus.identity();
  }

  /**
   * 复制到一个新的实体上
   *
   * @param otherEntity 另一个实体
   * @return 返回 {@code <T extends Entity>}
   */
  @SuppressWarnings("unchecked")
  public final <T extends Entity> T copyTo(@Nullable Entity otherEntity) {
    if (otherEntity == null) {
      return copyTo();
    }
    Copy.bean(this, otherEntity);
    return (T) otherEntity;
  }

  /**
   * 复制到一个新的实体上
   *
   * @return 返回 {@code <T extends Entity>}
   */
  @SuppressWarnings("unchecked")
  public final <T extends Entity> T copyTo() {
    Class<? extends Entity> clz = getClass();
    Object instance = New.instance(clz);
    Copy.bean(this, instance);
    return (T) instance;
  }

  /**
   * 从另一个实体复制过来
   *
   * @param otherEntity 另一个实体
   * @return 返回 {@code <T extends Entity>}
   */
  @SuppressWarnings("unchecked")
  public final <T extends Entity> T copy(Object otherEntity) {
    Copy.bean(otherEntity, this);
    return (T) this;
  }
  /**
   * 复制实体到 {@code Map}上
   *
   * @return {@code Map<String, Object>}
   */
  @SuppressWarnings("unchecked")
  public final Map<String, Object> toLazyMap() {
    return Copy.beanForMap(this);
  }
  /**
   * 复制实体到 {@code Map}上
   *
   * @return {@code Map<String, Object>}
   */
  @SuppressWarnings("unchecked")
  public final Map<String, Object> toMap() {
    return new HashMap<>(Copy.beanForMap(this));
  }

  /**
   * 复制实体到 {@code Map}上
   *
   * @return {@code Map<String, Object>}
   */
  @SuppressWarnings("unchecked")
  public final Map<String, Object> toCaseInsensitiveMap() {
    return new CaseInsensitiveMap<>(Copy.beanForMap(this));
  }
  /**
   * 从{@code Map}复制到实体上
   *
   * @return {@code Map<String, Object>}
   */
  @SuppressWarnings("unchecked")
  public final <T extends Entity> T fromMap(Map<String, Object> container) {
    Copy.mapForBean(this, container);
    return (T) this;
  }

  @Override
  public void clear() {}

  /**
   * 输出重要信息
   *
   * @return {@code String}
   */
  public final String toImportantString() {
    return "";
  }

  @Override
  public String toString() {
    return super.toString();
  }

  public static class Query {

    /** 状态 */
    private Integer status;

    public Integer getStatus() {
      return status;
    }

    public void setStatus(Integer status) {
      this.status = status;
    }
  }
}
