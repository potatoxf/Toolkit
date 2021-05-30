package pxf.infrastructure.system.task.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.List;
import pxf.infrastructure.system.task.entity.SystemTaskLog;
import pxf.infrastructure.system.task.entity.SystemTaskLog.Query;
import pxf.infrastructure.system.task.mapper.SystemTaskLogMapper;

/**
 * @author potatoxf
 * @date 2021/5/7
 */
public class SystemTaskLogServiceImpl extends ServiceImpl<SystemTaskLogMapper, SystemTaskLog>
    implements SystemTaskLogService {

  /**
   * 查询系统任务列表
   *
   * @param query 查询条件
   * @return {@code List<SystemTaskLog>}
   */
  @Override
  public List<SystemTaskLog> queryTaskLogList(Query query) {
    QueryWrapper<SystemTaskLog> queryWrapper = buildSystemTaskLogQueryWrapper(query);
    return getBaseMapper().selectList(queryWrapper);
  }

  /**
   * 查询系统任务列表
   *
   * @param page {@code IPage<SystemTaskLog>}
   * @param query 查询条件
   * @return {@code List<SystemTaskLog>}
   */
  @Override
  public IPage<SystemTaskLog> queryTaskLogList(IPage<SystemTaskLog> page, Query query) {
    QueryWrapper<SystemTaskLog> queryWrapper = buildSystemTaskLogQueryWrapper(query);
    return getBaseMapper().selectPage(page, queryWrapper);
  }

  private QueryWrapper<SystemTaskLog> buildSystemTaskLogQueryWrapper(Query query) {
    if (query == null) {
      return null;
    }
    QueryWrapper<SystemTaskLog> queryWrapper = new QueryWrapper<>();
    Long taskId = query.getTaskId();
    if (taskId != null) {
      queryWrapper.eq("task_id", taskId);
    }
    String taskName = query.getTaskName();
    if (taskName != null) {
      queryWrapper.like("task_name", taskName);
    }
    Integer processStatus = query.getProcessStatus();
    if (processStatus != null) {
      queryWrapper.eq("process_status", processStatus);
    }
    String executor = query.getExecutor();
    if (executor != null) {
      queryWrapper.like("executor", executor);
    }
    String executorIpAddress = query.getExecutorIpAddress();
    if (executorIpAddress != null) {
      queryWrapper.like("executor_ip_address", executorIpAddress);
    }
    Date launchDatetimeStart = query.getLaunchDatetimeStart();
    if (launchDatetimeStart != null) {
      queryWrapper.gt(true, "launch_datetime", launchDatetimeStart);
    }
    Date launchDatetimeEnd = query.getLaunchDatetimeEnd();
    if (launchDatetimeEnd != null) {
      queryWrapper.lt(true, "launch_datetime", launchDatetimeEnd);
    }
    Date finishDatetimeStart = query.getFinishDatetimeStart();
    if (finishDatetimeStart != null) {
      queryWrapper.gt(true, "finish_datetime", finishDatetimeStart);
    }
    Date finishDatetimeEnd = query.getFinishDatetimeEnd();
    if (finishDatetimeEnd != null) {
      queryWrapper.lt(true, "finish_datetime", finishDatetimeEnd);
    }
    String processLog = query.getProcessLog();
    if (processLog != null) {
      queryWrapper.like(true, "process_log", processLog);
    }
    return queryWrapper;
  }
}
