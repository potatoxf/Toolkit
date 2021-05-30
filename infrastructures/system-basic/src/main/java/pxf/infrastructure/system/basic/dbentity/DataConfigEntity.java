package pxf.infrastructure.system.basic.dbentity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 数据Bean基类
 *
 * @author potatoxf
 * @date 2021/3/21
 */
public abstract class DataConfigEntity<
        SUB extends DataConfigEntity<SUB, ID>, ID extends Number & Comparable<ID>>
    extends DataRecordEntity<SUB, ID> {

  /** 备注 */
  @TableField(
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String comment;
  /** 额外参数字符串 */
  @TableField(
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String param;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

  public static class Query extends DataRecordEntity.Query {
    /** 备注 */
    private String remark;
    /** 额外参数字符串 */
    private String param;

    public String getRemark() {
      return remark;
    }

    public void setRemark(String remark) {
      this.remark = remark;
    }

    public String getParam() {
      return param;
    }

    public void setParam(String param) {
      this.param = param;
    }
  }
}
