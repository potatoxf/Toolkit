package pxf.infrastructure.system.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import pxf.infrastructure.system.task.entity.SystemTaskLog;

/**
 * @author potatoxf
 * @date 2021/5/7
 */
public interface SystemTaskLogService extends IService<SystemTaskLog> {

  /**
   * 查询系统任务列表
   *
   * @param query 查询条件
   * @return {@code List<SystemTaskLog>}
   */
  List<SystemTaskLog> queryTaskLogList(SystemTaskLog.Query query);


  /**
   * 查询系统任务列表
   *
   * @param page  {@code IPage<SystemTaskLog>}
   * @param query 查询条件
   * @return {@code List<SystemTaskLog>}
   */
  IPage<SystemTaskLog> queryTaskLogList(IPage<SystemTaskLog> page, SystemTaskLog.Query query);
}
