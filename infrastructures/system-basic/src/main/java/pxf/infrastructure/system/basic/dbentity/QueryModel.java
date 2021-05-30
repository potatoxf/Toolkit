package pxf.infrastructure.system.basic.dbentity;

import java.util.Date;

/**
 * 查询模块
 *
 * @author potatoxf
 * @date 2021/5/13
 */
public abstract class QueryModel extends Entity {
  /** 状态 */
  private Integer status;
  /** 最近一次更新时间开始 */
  private Date updateDatetimeStart;
  /** 最近一次更新时间 结束 */
  private Date updateDatetimeEnd;
  /** 最近一次更新者 */
  private String updater;
  /** 创建时间开始 */
  private Date createDatetimeStart;
  /** 创建时间结束 */
  private Date createDatetimeEnd;
  /** 创建者 */
  private String creator;
  /** 备注 */
  private String remark;
  /** 额外参数字符串 */
  private String param;

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
