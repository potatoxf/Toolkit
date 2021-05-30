package pxf.infrastructure.system.task.quartz;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.infrastructure.system.task.ISystemTask;
import pxf.infrastructure.system.task.SystemTaskExecutorSupplier;
import pxf.infrastructure.system.task.SystemTaskStatus;
import pxf.infrastructure.system.task.TaskException;
import pxf.infrastructure.system.task.entity.SystemTask;
import pxf.infrastructure.system.task.entity.SystemTaskLog;
import pxf.infrastructure.system.task.service.SystemTaskLogService;
import pxf.infrastructure.system.task.service.SystemTaskService;
import pxf.toolkit.basic.reflect.DefaultInvocationTargetActuator;
import pxf.toolkit.basic.reflect.InvocationTarget;
import pxf.toolkit.basic.reflect.InvocationTargetActuator;
import pxf.toolkit.basic.reflect.InvocationTargetActuatorChain;
import pxf.toolkit.basic.reflect.InvocationTargetActuatorChainBuilder;
import pxf.toolkit.basic.reflect.ReflectHelper;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.Extract;
import pxf.toolkit.basic.util.Get;

/**
 * Quartz分发任务
 *
 * @author potatoxf
 * @date 2021/5/6
 */
public class QuartzDispatcherJob implements Job {

  private static final Logger LOG = LoggerFactory.getLogger(QuartzDispatcherJob.class);
  private static final String NO_EXIST = "NO EXIST";

  private static QuartzSystemTaskManagerService managerService;

  private static InvocationTargetActuatorChainBuilder actuatorChainBuilder =
      new InvocationTargetActuatorChainBuilder();

  static {
    actuatorChainBuilder.register(new DefaultInvocationTargetActuator());
    actuatorChainBuilder.register(new DefaultTaskInvocationTargetActuator());
  }

  public static void setInvocationTargetActuatorChainBuilder(
      InvocationTargetActuatorChainBuilder invocationTargetActuatorChainBuilder) {
    QuartzDispatcherJob.actuatorChainBuilder =
        Objects.requireNonNull(invocationTargetActuatorChainBuilder);
  }

  public static void setQuartzSystemTaskManagerService(
      QuartzSystemTaskManagerService quartzSystemTaskManagerService) {
    QuartzDispatcherJob.managerService = Objects.requireNonNull(quartzSystemTaskManagerService);
  }

  /** 系统任务管理服务 */
  protected QuartzSystemTaskManagerService systemTaskManagerService;
  /** 系统任务服务 */
  protected SystemTaskService systemTaskService;
  /** 系统任务日志服务 */
  protected SystemTaskLogService systemTaskLogService;
  /** Quartz键提供器 */
  private QuartzKeySupplier quartzKeySupplier;

  @Override
  public final void execute(JobExecutionContext context) throws JobExecutionException {
    if (managerService == null) {
      throw new IllegalArgumentException("Management service must not be null");
    }
    systemTaskManagerService = getSystemTaskManagerService();
    systemTaskService = getSystemTaskService();
    systemTaskLogService = getSystemTaskLogService();
    quartzKeySupplier = getQuartzKeySupplier();
    // 获取数据
    JobDataMap dataMap = context.getMergedJobDataMap();
    // 获取任务
    JobDetail jobDetail = context.getJobDetail();
    Long taskId = quartzKeySupplier.getTaskIdByJobKey(jobDetail.getKey());
    SystemTask systemTask = managerService.getTaskAndCheck(taskId);
    SystemTaskLog systemTaskLog = new SystemTaskLog();
    systemTaskLog.setTaskId(taskId);
    systemTaskLog.setIpAddress(Get.localIP());
    if (systemTask == null) {
      // 处理不存在日志
      buildNoExistTaskLog(systemTaskLog, dataMap);
      systemTaskLogService.saveOrUpdate(systemTaskLog);
      return;
    }
    buildStartLog(systemTaskLog, dataMap, systemTask);
    systemTaskLogService.saveOrUpdate(systemTaskLog);

    systemTask.setTaskStatus(SystemTaskStatus.RUNNING.identity());
    managerService.saveTask(systemTask);

    Runnable runnable =
        createRunnable(systemTask, dataMap.getString(quartzKeySupplier.getQuartzParamsKey()));

    long start = System.currentTimeMillis();
    try {
      runnable.run();
    } catch (Throwable e) {
      if (LOG.isErrorEnabled()) {
        LOG.error(
            String.format("System task [%s] was executed error", systemTask.getTaskName()), e);
      }
      buildExceptionLog(systemTaskLog, dataMap, systemTask, e);
    } finally {
      long end = System.currentTimeMillis();
      buildFinishLog(systemTaskLog, dataMap, systemTask, end - start);
      systemTaskLogService.saveOrUpdate(systemTaskLog);
    }
  }

  protected Runnable createRunnable(SystemTask systemTask, String param) {
    InvocationTarget invocationTarget = InvocationTarget.of(systemTask.getTaskTarget());
    InvocationTargetActuatorChain invocationTargetActuatorChain = actuatorChainBuilder.build();
    return () -> {
      try {
        invocationTargetActuatorChain.execute(invocationTarget, param);
      } catch (InvocationTargetException e) {
        throw new TaskException(e);
      }
    };
  }

  protected SystemTaskService getSystemTaskService() {
    return managerService.getSystemTaskService();
  }

  protected SystemTaskLogService getSystemTaskLogService() {
    return managerService.getSystemTaskLogService();
  }

  protected QuartzKeySupplier getQuartzKeySupplier() {
    return managerService.getQuartzKeySupplier();
  }

  protected QuartzSystemTaskManagerService getSystemTaskManagerService() {
    return managerService;
  }

  private void buildNoExistTaskLog(SystemTaskLog systemTaskLog, JobDataMap dataMap) {
    systemTaskLog.setProcessStatus(SystemTaskStatus.EXCEPTION.identity());
    systemTaskLog.setTaskName(NO_EXIST);
    systemTaskLog.setExecutor(getExecutor(null));
    systemTaskLog.setExecutorIpAddress(getExecutorIpAddress(null));
    systemTaskLog.setProcessLog(NO_EXIST);
  }

  private void buildStartLog(
      SystemTaskLog systemTaskLog, JobDataMap dataMap, SystemTask systemTask) {
    systemTaskLog.setProcessStatus(SystemTaskStatus.RUNNING.identity());
    systemTaskLog.setTaskName(systemTask.getTaskName());
    systemTaskLog.setTaskParams(dataMap.getString(quartzKeySupplier.getQuartzParamsKey()));
    systemTaskLog.setExecutor(getExecutor(systemTask));
    systemTaskLog.setExecutorIpAddress(getExecutorIpAddress(systemTask));
    systemTaskLog.setLaunchDatetime(new Date());
  }

  private void buildFinishLog(
      SystemTaskLog systemTaskLog, JobDataMap dataMap, SystemTask systemTask, long time) {
    systemTaskLog.setProcessStatus(SystemTaskStatus.NORMAL.identity());
    systemTaskLog.setFinishDatetime(new Date());
    systemTaskLog.setProcessDuration(String.valueOf(time));
  }

  private void buildExceptionLog(
      SystemTaskLog systemTaskLog, JobDataMap dataMap, SystemTask systemTask, Throwable throwable) {
    systemTaskLog.setProcessStatus(SystemTaskStatus.EXCEPTION.identity());
    systemTaskLog.setProcessLog(Extract.exceptionInformation(throwable));
  }

  private String getExecutor(SystemTask systemTask) {
    SystemTaskExecutorSupplier systemTaskExecutorSupplier =
        systemTaskManagerService.getSystemTaskExecutorSupplier();
    if (systemTaskExecutorSupplier == null) {
      if (systemTask == null) {
        return null;
      }
      return systemTask.getDefaultExecutor();
    }
    return systemTaskExecutorSupplier.getExecutor();
  }

  private String getExecutorIpAddress(SystemTask systemTask) {
    SystemTaskExecutorSupplier systemTaskExecutorSupplier =
        systemTaskManagerService.getSystemTaskExecutorSupplier();
    if (systemTaskExecutorSupplier == null) {
      if (systemTask == null) {
        return null;
      }
      return systemTask.getDefaultExecutorIpAddress();
    }
    return systemTaskExecutorSupplier.getExecutorIpAddress();
  }

  private static class DefaultTaskInvocationTargetActuator implements InvocationTargetActuator {

    /**
     * 执行
     *
     * @param invocationTarget 调用目标
     * @param invocationTargetActuatorChain 执行链
     * @param args 参数
     * @return 返回值
     * @throws InvocationTargetException 当调用目标发生错误时
     */
    @Nullable
    @Override
    public Object execute(
        @Nonnull InvocationTarget invocationTarget,
        @Nonnull InvocationTargetActuatorChain invocationTargetActuatorChain,
        @Nullable Object... args)
        throws InvocationTargetException {
      Class<?> type = invocationTarget.getType();
      if (type != null && invocationTarget.getMethodName() == null) {
        if (ISystemTask.class.isAssignableFrom(type)) {
          ISystemTask task = (ISystemTask) ReflectHelper.newInstance(type);
          try {
            task.execute(args != null && args.length != 0 ? String.valueOf(args[0]) : Empty.STRING);
            return null;
          } catch (Exception e) {
            throw new TaskException(e);
          }
        }
      }
      return invocationTargetActuatorChain.execute(invocationTarget, args);
    }
  }
}
