package pxf.infrastructure.system.basic.dbentity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import pxf.toolkit.basic.util.Compare;

/**
 * 数据Bean基类
 *
 * @author potatoxf
 * @date 2021/3/21
 */
public abstract class BigDataEntity<SUB extends BigDataEntity<SUB>> extends Entity
    implements Comparable<SUB> {

  /** 主键 */
  @TableId(type = IdType.NONE)
  private String id;

  /** 乐观锁 */
  @TableField(
      fill = FieldFill.INSERT_UPDATE,
      condition = SqlCondition.EQUAL,
      insertStrategy = FieldStrategy.NOT_NULL,
      updateStrategy = FieldStrategy.NOT_NULL,
      update = "%s+1")
  private Integer version;

  @Override
  public int compareTo(SUB other) {
    return Compare.objectOrComparableForPriorityNull(getId(), other.getId());
  }

  @Override
  public void clear() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
