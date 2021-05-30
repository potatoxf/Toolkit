package pxf.infrastructure.system.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import org.quartz.CronExpression;
import pxf.infrastructure.system.basic.dbentity.DataRecordEntity;

/**
 * @author potatoxf
 * @date 2021/5/6
 */
@TableName("pxf_sys_task")
public class SystemTask extends DataRecordEntity<SystemTask, Long> {

  /** 任务名称参数 */
  private String taskName;
  /** 目标任务 */
  private String taskTarget;
  /** 任务参数 */
  private String taskParams;
  /** cron */
  private String taskCron;
  /** 任务状态 */
  private Integer taskStatus;
  /** 备注 */
  private String remark;
  /** 默认执行者 */
  private String defaultExecutor;
  /** 默认执行人的IP地址 */
  private String defaultExecutorIpAddress;


  // --------------------------------------------------------------------------- get set

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getTaskTarget() {
    return taskTarget;
  }

  public void setTaskTarget(String taskTarget) {
    this.taskTarget = taskTarget;
  }

  public String getTaskParams() {
    return taskParams;
  }

  public void setTaskParams(String taskParams) {
    this.taskParams = taskParams;
  }

  public String getTaskCron() {
    return taskCron;
  }

  public void setTaskCron(String taskCron) {
    if (!CronExpression.isValidExpression(taskCron)) {
      throw new IllegalArgumentException("Cron expression error");
    }
    this.taskCron = taskCron;
  }

  public Integer getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(Integer taskStatus) {
    this.taskStatus = taskStatus;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDefaultExecutor() {
    return defaultExecutor;
  }

  public void setDefaultExecutor(String defaultExecutor) {
    this.defaultExecutor = defaultExecutor;
  }

  public String getDefaultExecutorIpAddress() {
    return defaultExecutorIpAddress;
  }

  public void setDefaultExecutorIpAddress(String defaultExecutorIpAddress) {
    this.defaultExecutorIpAddress = defaultExecutorIpAddress;
  }

  public static class Query {

    /** 任务名称参数 */
    private String taskName;
    /** 目标任务 */
    private String taskTarget;
    /** 任务状态 */
    private Integer taskStatus;
    /** 备注 */
    private String remark;
    /** 更新时间-开始 */
    private Date updateDatetimeStart;
    /** 更新时间-结束 */
    private Date updateDatetimeEnd;
    /** 更新者 */
    private String updater;
    /** 创建时间-开始 */
    private Date createDatetimeStart;
    /** 创建时间-结束 */
    private Date createDatetimeEnd;
    /** 创建者 */
    private String creator;

    public String getTaskName() {
      return taskName;
    }

    public void setTaskName(String taskName) {
      this.taskName = taskName;
    }

    public String getTaskTarget() {
      return taskTarget;
    }

    public void setTaskTarget(String taskTarget) {
      this.taskTarget = taskTarget;
    }

    public Integer getTaskStatus() {
      return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
      this.taskStatus = taskStatus;
    }

    public String getRemark() {
      return remark;
    }

    public void setRemark(String remark) {
      this.remark = remark;
    }

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
