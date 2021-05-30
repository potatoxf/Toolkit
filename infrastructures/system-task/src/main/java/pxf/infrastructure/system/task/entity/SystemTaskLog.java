package pxf.infrastructure.system.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import pxf.infrastructure.system.basic.dbentity.DataRecordEntity;
import pxf.toolkit.basic.util.TimeHelper;

/**
 * @author potatoxf
 * @date 2021/5/6
 */
@TableName("pxf_sys_task_log")
public class SystemTaskLog extends DataRecordEntity<SystemTaskLog, Long> {

  /** 任务ID */
  private Long taskId;
  /** 任务名称 */
  private String taskName;
  /** 任务参数 */
  private String taskParams;
  /** 任务处理状态 */
  private Integer processStatus;
  /** 执行人的IP地址 */
  private String ipAddress;
  /** 执行者 */
  private String executor;
  /** 执行人的IP地址 */
  private String executorIpAddress;
  /** 启动时间 */
  private Date launchDatetime;
  /** 完成时间 */
  private Date finishDatetime;
  /** 任务时长 */
  private String processDuration;
  /** 处理日志 */
  private String processLog;

  @Override
  public String toString() {
    String ls = System.lineSeparator();
    return "EXECUTOR LOG"
        + "<============================>"
        + ls
        + executor
        + " ON ["
        + executorIpAddress
        + "] EXECUTE TASK FOR ["
        + taskName
        + "] ON ["
        + ipAddress
        + "]"
        + ls
        + "START TIME : "
        + (launchDatetime != null ? TimeHelper.formatDefaultDatetime(launchDatetime) : "ERROR TIME")
        + ls
        + "END TIME   : "
        + (launchDatetime != null ? TimeHelper.formatDefaultDatetime(launchDatetime) : "ERROR TIME")
        + ls
        + "TOTAL TIME : "
        + processDuration
        + ls
        + "PARAMETRIC : "
        + (taskParams != null ? taskParams : "")
        + ls
        + "EXECUTOR LOG"
        + "<---------------------------->"
        + ls;
  }

  // --------------------------------------------------------------------------- get set

  public Long getTaskId() {
    return taskId;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getTaskParams() {
    return taskParams;
  }

  public void setTaskParams(String taskParams) {
    this.taskParams = taskParams;
  }

  public Integer getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(Integer processStatus) {
    this.processStatus = processStatus;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getExecutor() {
    return executor;
  }

  public void setExecutor(String executor) {
    this.executor = executor;
  }

  public String getExecutorIpAddress() {
    return executorIpAddress;
  }

  public void setExecutorIpAddress(String executorIpAddress) {
    this.executorIpAddress = executorIpAddress;
  }

  public Date getLaunchDatetime() {
    return launchDatetime;
  }

  public void setLaunchDatetime(Date launchDatetime) {
    this.launchDatetime = launchDatetime;
  }

  public Date getFinishDatetime() {
    return finishDatetime;
  }

  public void setFinishDatetime(Date finishDatetime) {
    this.finishDatetime = finishDatetime;
  }

  public String getProcessDuration() {
    return processDuration;
  }

  public void setProcessDuration(String processDuration) {
    this.processDuration = processDuration;
  }

  public String getProcessLog() {
    return processLog;
  }

  public void setProcessLog(String processLog) {
    this.processLog = processLog;
  }

  public static class Query {

    /** 任务ID */
    private Long taskId;
    /** 任务名称 */
    private String taskName;
    /** 任务处理状态 */
    private Integer processStatus;
    /** 执行者 */
    private String executor;
    /** 执行人的IP地址 */
    private String executorIpAddress;
    /** 启动时间-开始 */
    private Date launchDatetimeStart;
    /** 启动时间-结束 */
    private Date launchDatetimeEnd;
    /** 完成时间-开始 */
    private Date finishDatetimeStart;
    /** 完成时间-结束 */
    private Date finishDatetimeEnd;
    /** 处理日志 */
    private String processLog;

    public Long getTaskId() {
      return taskId;
    }

    public void setTaskId(Long taskId) {
      this.taskId = taskId;
    }

    public String getTaskName() {
      return taskName;
    }

    public void setTaskName(String taskName) {
      this.taskName = taskName;
    }

    public Integer getProcessStatus() {
      return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
      this.processStatus = processStatus;
    }

    public String getExecutor() {
      return executor;
    }

    public void setExecutor(String executor) {
      this.executor = executor;
    }

    public String getExecutorIpAddress() {
      return executorIpAddress;
    }

    public void setExecutorIpAddress(String executorIpAddress) {
      this.executorIpAddress = executorIpAddress;
    }

    public Date getLaunchDatetimeStart() {
      return launchDatetimeStart;
    }

    public void setLaunchDatetimeStart(Date launchDatetimeStart) {
      this.launchDatetimeStart = launchDatetimeStart;
    }

    public Date getLaunchDatetimeEnd() {
      return launchDatetimeEnd;
    }

    public void setLaunchDatetimeEnd(Date launchDatetimeEnd) {
      this.launchDatetimeEnd = launchDatetimeEnd;
    }

    public Date getFinishDatetimeStart() {
      return finishDatetimeStart;
    }

    public void setFinishDatetimeStart(Date finishDatetimeStart) {
      this.finishDatetimeStart = finishDatetimeStart;
    }

    public Date getFinishDatetimeEnd() {
      return finishDatetimeEnd;
    }

    public void setFinishDatetimeEnd(Date finishDatetimeEnd) {
      this.finishDatetimeEnd = finishDatetimeEnd;
    }

    public String getProcessLog() {
      return processLog;
    }

    public void setProcessLog(String processLog) {
      this.processLog = processLog;
    }
  }
}
