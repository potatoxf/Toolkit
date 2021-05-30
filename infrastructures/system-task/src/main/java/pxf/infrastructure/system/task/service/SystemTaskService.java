package pxf.infrastructure.system.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import pxf.infrastructure.system.task.entity.SystemTask;

/**
 * @author potatoxf
 * @date 2021/5/7
 */
public interface SystemTaskService extends IService<SystemTask> {

  /**
   * 查询列表
   *
   * @param query 查询条件
   * @return {@code List<SystemTask>}
   */
  List<SystemTask> queryTaskList(SystemTask.Query query);

  /**
   * 查询列表
   *
   * @param page  {@code IPage<SystemTask>}
   * @param query 查询条件
   * @return {@code List<SystemTask>}
   */
  IPage<SystemTask> queryTaskList(IPage<SystemTask> page, SystemTask.Query query);
}
