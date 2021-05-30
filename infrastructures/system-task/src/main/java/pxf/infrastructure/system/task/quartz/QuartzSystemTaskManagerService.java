package pxf.infrastructure.system.task.quartz;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.infrastructure.system.task.SystemTaskExecutorSupplier;
import pxf.infrastructure.system.task.SystemTaskManagerService;
import pxf.infrastructure.system.task.SystemTaskStatus;
import pxf.infrastructure.system.task.TaskException;
import pxf.infrastructure.system.task.entity.SystemTask;
import pxf.infrastructure.system.task.entity.SystemTaskLog;
import pxf.infrastructure.system.task.service.SystemTaskLogService;
import pxf.infrastructure.system.task.service.SystemTaskService;
import pxf.toolkit.basic.util.Const;

/**
 * @author potatoxf
 * @date 2021/5/6
 */
public class QuartzSystemTaskManagerService implements SystemTaskManagerService {

  private static final Logger LOG = LoggerFactory.getLogger(QuartzSystemTaskManagerService.class);
  /** 调度器 */
  private final Scheduler scheduler;
  /** 系统任务服务 */
  private final SystemTaskService systemTaskService;
  /** 系统任务日志服务 */
  private final SystemTaskLogService systemTaskLogService;
  /** Quartz键提供器 */
  private final QuartzKeySupplier quartzKeySupplier;
  /** Job类 */
  private final Class<? extends QuartzDispatcherJob> jobClass;
  /** 任务执行者信息 */
  private volatile SystemTaskExecutorSupplier systemTaskExecutorSupplier;

  public QuartzSystemTaskManagerService(
      Scheduler scheduler,
      SystemTaskService systemTaskService,
      SystemTaskLogService systemTaskLogService,
      QuartzKeySupplier quartzKeySupplier) {
    this(scheduler, systemTaskService, systemTaskLogService, quartzKeySupplier, null);
  }

  public QuartzSystemTaskManagerService(
      Scheduler scheduler,
      SystemTaskService systemTaskService,
      SystemTaskLogService systemTaskLogService,
      QuartzKeySupplier quartzKeySupplier,
      Class<? extends QuartzDispatcherJob> jobClass) {
    this.scheduler = Objects.requireNonNull(scheduler);
    this.systemTaskService = Objects.requireNonNull(systemTaskService);
    this.systemTaskLogService = Objects.requireNonNull(systemTaskLogService);
    this.quartzKeySupplier = Objects.requireNonNull(quartzKeySupplier);
    this.jobClass = jobClass == null ? QuartzDispatcherJob.class : jobClass;
  }

  /**
   * 获取任务执行者信息
   *
   * @return {@code SystemTaskExecutorSupplier}
   */
  @Nullable
  @Override
  public SystemTaskExecutorSupplier getSystemTaskExecutorSupplier() {
    return systemTaskExecutorSupplier;
  }

  @Override
  public boolean checkExist(Long taskId) {
    if (taskId == null) {
      return false;
    }
    JobKey jobKey = quartzKeySupplier.getJobKey(taskId);
    try {
      return scheduler.checkExists(jobKey);
    } catch (SchedulerException e) {
      throw new TaskException(e);
    }
  }

  /**
   * 检查任务是否存在
   *
   * @param taskId 任务ID
   * @return 如果存在返回 {@code true}，否则 {@code false}
   */
  @Override
  public boolean checkExistAndRemove(Long taskId) {
    if (taskId == null) {
      return false;
    }
    JobKey jobKey = quartzKeySupplier.getJobKey(taskId);
    try {
      boolean result = scheduler.checkExists(jobKey);
      if (!result) {
        removeTask(taskId);
      }
      return result;
    } catch (SchedulerException e) {
      throw new TaskException(e);
    }
  }

  /**
   * 获取任务并检查
   *
   * @param taskId 任务ID
   * @return {@code SystemTask}
   */
  @Override
  public SystemTask getTaskAndCheck(Long taskId) {
    if (checkTaskId(taskId)) {
      return null;
    }
    SystemTask systemTask = systemTaskService.getById(taskId);
    if (systemTask == null) {
      if (LOG.isWarnEnabled()) {
        LOG.warn(String.format("Task ID [%d] is not exist", taskId));
      }
      return null;
    }
    return systemTask;
  }
  /**
   * 创建任务
   *
   * @param scheduler 调度器
   * @param systemTask 任务
   */
  @Override
  public void createTask(Scheduler scheduler, SystemTask systemTask) {
    createQuartzTask(scheduler, systemTask);
  }

  /**
   * 保存
   *
   * @param systemTask {@code SystemTask}
   */
  @Override
  public void saveTask(SystemTask systemTask) {
    systemTaskService.saveOrUpdate(systemTask);
    createOrModifyQuartzTask(systemTask);
  }

  /**
   * 保存或更新
   *
   * @param systemTask {@code SystemTask}
   */
  @Override
  public void alterTask(SystemTask systemTask) {
    SystemTask oldSystemTask = getTaskAndCheck(systemTask.getId());
    if (oldSystemTask == null) {
      systemTask.setId(null);
      saveTask(systemTask);
    } else {
      systemTaskService.updateById(systemTask);
      createOrModifyQuartzTask(systemTask);
    }
  }

  /**
   * 删除任务
   *
   * @param taskId 任务ID
   */
  @Override
  public void removeTask(Long taskId) {
    if (checkTaskId(taskId)) {
      return;
    }
    boolean b = systemTaskService.removeById(taskId);
    if (b) {
      SystemTaskLog.Query query = new SystemTaskLog.Query();
      query.setTaskId(taskId);
      List<Long> ids =
          systemTaskLogService.queryTaskLogList(query).stream()
              .map(SystemTaskLog::getId)
              .collect(Collectors.toList());
      systemTaskService.removeByIds(ids);
    }
    removeQuartzTask(scheduler, taskId);
  }

  /**
   * 立即运行
   *
   * @param taskId 任务ID
   */
  @Override
  public void runTask(Long taskId) {
    runTask(taskId, null);
  }

  /**
   * 立即运行
   *
   * @param taskId 任务ID
   * @param param 参数
   */
  @Override
  public void runTask(Long taskId, String param) {
    SystemTask systemTask = getTaskAndCheck(taskId);
    if (systemTask == null) {
      return;
    }
    systemTask.setTaskStatus(SystemTaskStatus.RUNNING.identity());
    systemTaskService.updateById(systemTask);
    runQuartzTask(scheduler, systemTask, param);
  }

  /**
   * 暂停运行
   *
   * @param taskId 任务ID
   */
  @Override
  public void pauseTask(Long taskId) {
    SystemTask systemTask = getTaskAndCheck(taskId);
    if (systemTask == null) {
      return;
    }
    systemTask.setTaskStatus(SystemTaskStatus.PAUSE.identity());
    systemTaskService.updateById(systemTask);
    pauseQuartzTask(scheduler, systemTask);
  }

  /**
   * 恢复任务
   *
   * @param taskId 任务ID
   */
  @Override
  public void resumeTask(Long taskId) {
    SystemTask systemTask = getTaskAndCheck(taskId);
    if (systemTask == null) {
      return;
    }
    systemTask.setTaskStatus(SystemTaskStatus.NORMAL.identity());
    systemTaskService.updateById(systemTask);
    resumeQuartzTask(scheduler, systemTask);
  }

  public SystemTaskService getSystemTaskService() {
    return systemTaskService;
  }

  public SystemTaskLogService getSystemTaskLogService() {
    return systemTaskLogService;
  }

  public QuartzKeySupplier getQuartzKeySupplier() {
    return quartzKeySupplier;
  }

  public void setSystemTaskExecutorSupplier(
      SystemTaskExecutorSupplier systemTaskExecutorSupplier) {
    this.systemTaskExecutorSupplier = systemTaskExecutorSupplier;
  }

  /**
   * 运行Quartz任务
   *
   * @param scheduler 调度器
   * @param systemTask 系统任务
   * @param param 参数
   * @throws TaskException 任务异常
   */
  private void runQuartzTask(Scheduler scheduler, SystemTask systemTask, String param) {
    try {
      JobDataMap dataMap = buildJobDataMap(systemTask, param);
      JobKey jobKey = quartzKeySupplier.getJobKey(systemTask.getId());
      if (!scheduler.checkExists(jobKey)) {
        createTask(scheduler, systemTask);
        scheduler.triggerJob(jobKey, dataMap);
        return;
      }
      scheduler.triggerJob(jobKey, dataMap);
    } catch (SchedulerException e) {
      throw new TaskException(e);
    }
  }

  /**
   * 构建数据
   *
   * @param systemTask 系统任务
   * @param param 参数
   * @return {@code JobDataMap}
   */
  protected JobDataMap buildJobDataMap(SystemTask systemTask, String param) {
    JobDataMap dataMap = new JobDataMap();
    dataMap.put(
        quartzKeySupplier.getQuartzParamsKey(), param == null ? systemTask.getTaskParams() : param);
    return dataMap;
  }

  /**
   * 暂停Quartz任务
   *
   * @param scheduler 调度器
   * @param systemTask 系统任务
   * @throws TaskException 任务异常
   */
  private void pauseQuartzTask(Scheduler scheduler, SystemTask systemTask) {
    try {
      JobKey jobKey = quartzKeySupplier.getJobKey(systemTask.getId());
      if (!scheduler.checkExists(jobKey)) {
        createTask(scheduler, systemTask);
        scheduler.pauseJob(jobKey);
        return;
      }
      scheduler.pauseJob(jobKey);
    } catch (SchedulerException e) {
      throw new TaskException(e);
    }
  }

  /**
   * 恢复Quartz任务
   *
   * @param scheduler 调度器
   * @param systemTask 系统任务
   * @throws TaskException 任务异常
   */
  private void resumeQuartzTask(Scheduler scheduler, SystemTask systemTask) {
    try {
      JobKey jobKey = quartzKeySupplier.getJobKey(systemTask.getId());
      if (!scheduler.checkExists(jobKey)) {
        createTask(scheduler, systemTask);
        return;
      }
      scheduler.resumeJob(jobKey);
    } catch (SchedulerException e) {
      throw new TaskException(e);
    }
  }

  /**
   * 创建或修改Quartz任务
   *
   * @param systemTask 系统任务
   */
  private void createOrModifyQuartzTask(SystemTask systemTask) {
    Long id = systemTask.getId();
    if (checkExist(id)) {
      modifyQuartzTask(scheduler, systemTask);
    } else {
      createQuartzTask(scheduler, systemTask);
    }
  }

  /**
   * 创建Quartz任务
   *
   * @param scheduler 调度器
   * @param systemTask 系统任务
   * @throws TaskException 任务异常
   */
  private void createQuartzTask(Scheduler scheduler, SystemTask systemTask) {
    try {
      JobKey jobKey = quartzKeySupplier.getJobKey(systemTask.getId());
      JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();

      CronScheduleBuilder scheduleBuilder =
          CronScheduleBuilder.cronSchedule(systemTask.getTaskCron())
              .withMisfireHandlingInstructionDoNothing();

      TriggerKey triggerKey = quartzKeySupplier.getTriggerKey(systemTask.getId());
      CronTrigger trigger =
          TriggerBuilder.newTrigger()
              .withIdentity(triggerKey)
              .withSchedule(scheduleBuilder)
              .build();

      JobDataMap jobDataMap = jobDetail.getJobDataMap();
      jobDataMap.putAll(buildJobDataMap(systemTask, null));

      scheduler.scheduleJob(jobDetail, trigger);
      checkStatusAndPause(scheduler, systemTask);
    } catch (SchedulerException e) {
      throw new TaskException(e);
    }
  }

  private void checkStatusAndPause(Scheduler scheduler, SystemTask systemTask) {
    // 如果更新之前任务是暂停状态，此时再次暂停任务
    Integer taskStatus = systemTask.getTaskStatus();
    if (taskStatus == null) {
      return;
    }
    SystemTaskStatus systemTaskStatus =
        Const.parseFindableConstant(SystemTaskStatus.class, taskStatus);
    if (SystemTaskStatus.PAUSE == systemTaskStatus) {
      pauseQuartzTask(scheduler, systemTask);
    }
  }

  /**
   * 修改Quartz任务
   *
   * @param scheduler 调度器
   * @param systemTask 系统任务
   * @throws TaskException 任务异常
   */
  private void modifyQuartzTask(Scheduler scheduler, SystemTask systemTask) {
    try {
      TriggerKey triggerKey = quartzKeySupplier.getTriggerKey(systemTask.getId());

      CronScheduleBuilder scheduleBuilder =
          CronScheduleBuilder.cronSchedule(systemTask.getTaskCron())
              .withMisfireHandlingInstructionDoNothing();

      CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

      trigger =
          trigger
              .getTriggerBuilder()
              .withIdentity(triggerKey)
              .withSchedule(scheduleBuilder)
              .build();

      trigger
          .getJobDataMap()
          .put(quartzKeySupplier.getQuartzParamsKey(), systemTask.getTaskParams());

      scheduler.rescheduleJob(triggerKey, trigger);

      checkStatusAndPause(scheduler, systemTask);
    } catch (SchedulerException e) {
      throw new TaskException(e);
    }
  }

  /**
   * 删除Quartz任务
   *
   * @param scheduler 调度器
   * @param taskId 任务ID
   * @throws TaskException 任务异常
   */
  private void removeQuartzTask(Scheduler scheduler, Long taskId) {
    try {
      JobKey jobKey = quartzKeySupplier.getJobKey(taskId);
      if (!scheduler.checkExists(jobKey)) {
        return;
      }
      scheduler.deleteJob(jobKey);
    } catch (SchedulerException e) {
      throw new TaskException(e);
    }
  }

  /**
   * 检查任务ID
   *
   * @param taskId 任务ID
   * @return 如果检查不成功返回 {@code true}，否则 {@code false}
   */
  private boolean checkTaskId(Long taskId) {
    if (taskId == null) {
      if (LOG.isErrorEnabled()) {
        LOG.error(String.format("Task ID [%d] is empty", taskId));
      }
      return true;
    }
    return false;
  }
}
