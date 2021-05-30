package pxf.infrastructure.system.basic.dbentity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 检测数据变化实体，一般用于较大的字段检测
 *
 * @author potatoxf
 * @date 2021/3/21
 */
public abstract class DataChangedEntity<
        SUB extends DataChangedEntity<SUB, ID>, ID extends Number & Comparable<ID>>
    extends DataEntity<SUB, ID> {

  /** 更新时间戳 */
  @TableField(
      fill = FieldFill.INSERT_UPDATE,
      condition = SqlCondition.EQUAL,
      insertStrategy = FieldStrategy.NOT_NULL,
      updateStrategy = FieldStrategy.NOT_NULL)
  private Long updateTimestamp;

  public Long getUpdateTimestamp() {
    return updateTimestamp;
  }

  public void setUpdateTimestamp(Long updateTimestamp) {
    this.updateTimestamp = updateTimestamp;
  }
}
