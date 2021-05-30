package pxf.infrastructure.system.basic.dbentity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;

/**
 * 数据Bean基类
 *
 * @author potatoxf
 * @date 2021/3/21
 */
public abstract class BigDataRecordEntity<SUB extends BigDataRecordEntity<SUB>>
    extends BigDataEntity<SUB> {

  /** 最近一次更新时间 */
  @TableField(
      fill = FieldFill.INSERT_UPDATE,
      insertStrategy = FieldStrategy.NOT_NULL,
      updateStrategy = FieldStrategy.NOT_NULL)
  private Date updateDatetime;
  /** 最近一次更新者 */
  @TableField(
      fill = FieldFill.INSERT_UPDATE,
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String updater;
  /** 创建时间 */
  @TableField(
      fill = FieldFill.INSERT,
      insertStrategy = FieldStrategy.NOT_NULL,
      updateStrategy = FieldStrategy.NOT_NULL)
  private Date createDatetime;
  /** 创建者 */
  @TableField(
      fill = FieldFill.INSERT,
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String creator;

  public Date getUpdateDatetime() {
    return updateDatetime;
  }

  public void setUpdateDatetime(Date updateDatetime) {
    this.updateDatetime = updateDatetime;
  }

  public String getUpdater() {
    return updater;
  }

  public void setUpdater(String updater) {
    this.updater = updater;
  }

  public Date getCreateDatetime() {
    return createDatetime;
  }

  public void setCreateDatetime(Date createDatetime) {
    this.createDatetime = createDatetime;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public static class Query extends Entity.Query {
    /** 最近一次更新时间 */
    private Date updateDatetimeStart;
    /** 最近一次更新时间 */
    private Date updateDatetimeEnd;
    /** 最近一次更新者 */
    private String updater;
    /** 创建时间 */
    private Date createDatetimeStart;
    /** 创建时间 */
    private Date createDatetimeEnd;
    /** 创建者 */
    private String creator;

    public Date getUpdateDatetimeStart() {
      return updateDatetimeStart;
    }

    public void setUpdateDatetimeStart(Date updateDatetimeStart) {
      this.updateDatetimeStart = updateDatetimeStart;
    }

    public Date getUpdateDatetimeEnd() {
      return updateDatetimeEnd;
    }

    public void setUpdateDatetimeEnd(Date updateDatetimeEnd) {
      this.updateDatetimeEnd = updateDatetimeEnd;
    }

    public String getUpdater() {
      return updater;
    }

    public void setUpdater(String updater) {
      this.updater = updater;
    }

    public Date getCreateDatetimeStart() {
      return createDatetimeStart;
    }

    public void setCreateDatetimeStart(Date createDatetimeStart) {
      this.createDatetimeStart = createDatetimeStart;
    }

    public Date getCreateDatetimeEnd() {
      return createDatetimeEnd;
    }

    public void setCreateDatetimeEnd(Date createDatetimeEnd) {
      this.createDatetimeEnd = createDatetimeEnd;
    }

    public String getCreator() {
      return creator;
    }

    public void setCreator(String creator) {
      this.creator = creator;
    }
  }
}
