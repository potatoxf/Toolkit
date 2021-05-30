package pxf.infrastructure.system.task.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.List;
import pxf.infrastructure.system.task.entity.SystemTask;
import pxf.infrastructure.system.task.mapper.SystemTaskMapper;

/**
 * @author potatoxf
 * @date 2021/5/7
 */
public class SystemTaskServiceImpl extends ServiceImpl<SystemTaskMapper, SystemTask>
    implements SystemTaskService {

  /**
   * 查询列表
   *
   * @param query 查询条件
   * @return {@code List<SystemTask>}
   */
  @Override
  public List<SystemTask> queryTaskList(SystemTask.Query query) {
    QueryWrapper<SystemTask> queryWrapper = buildSystemTaskQueryWrapper(query);
    return getBaseMapper().selectList(queryWrapper);
  }

  /**
   * 查询列表
   *
   * @param page {@code IPage<SystemTask>}
   * @param query 查询条件
   * @return {@code List<SystemTask>}
   */
  @Override
  public IPage<SystemTask> queryTaskList(IPage<SystemTask> page, SystemTask.Query query) {
    QueryWrapper<SystemTask> queryWrapper = buildSystemTaskQueryWrapper(query);
    return getBaseMapper().selectPage(page, queryWrapper);
  }

  private QueryWrapper<SystemTask> buildSystemTaskQueryWrapper(SystemTask.Query query) {
    if (query == null) {
      return null;
    }
    QueryWrapper<SystemTask> queryWrapper = new QueryWrapper<>();
    String taskName = query.getTaskName();
    if (taskName != null) {
      queryWrapper.like("task_name", taskName);
    }
    String taskTarget = query.getTaskTarget();
    if (taskTarget != null) {
      queryWrapper.like("task_target", taskTarget);
    }
    Integer taskStatus = query.getTaskStatus();
    if (taskStatus != null) {
      queryWrapper.eq("task_status", taskStatus);
    }
    String remark = query.getRemark();
    if (remark != null) {
      queryWrapper.like("remark", remark);
    }
    Date updateDatetimeStart = query.getUpdateDatetimeStart();
    if (updateDatetimeStart != null) {
      queryWrapper.gt(true, "update_datetime", updateDatetimeStart);
    }
    Date updateDatetimeEnd = query.getUpdateDatetimeEnd();
    if (updateDatetimeEnd != null) {
      queryWrapper.lt(true, "update_datetime", updateDatetimeEnd);
    }
    String updater = query.getUpdater();
    if (updater != null) {
      queryWrapper.like("updater", updater);
    }
    Date createDatetimeStart = query.getCreateDatetimeStart();
    if (createDatetimeStart != null) {
      queryWrapper.gt(true, "create_time", createDatetimeStart);
    }
    Date createDatetimeEnd = query.getCreateDatetimeEnd();
    if (createDatetimeEnd != null) {
      queryWrapper.lt(true, "create_time", createDatetimeEnd);
    }
    String creator = query.getCreator();
    if (creator != null) {
      queryWrapper.like("creator", creator);
    }
    return queryWrapper;
  }
}
