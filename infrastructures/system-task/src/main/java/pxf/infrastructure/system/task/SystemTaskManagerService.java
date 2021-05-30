package pxf.infrastructure.system.task;

import javax.annotation.Nullable;
import org.quartz.Scheduler;
import pxf.infrastructure.system.task.entity.SystemTask;

/**
 * 系统任务服务
 *
 * @author potatoxf
 * @date 2021/5/6
 */
public interface SystemTaskManagerService {

  /**
   * 获取任务执行者信息
   *
   * @return {@code SystemTaskExecutorSupplier}
   */
  @Nullable
  SystemTaskExecutorSupplier getSystemTaskExecutorSupplier();

  /**
   * 检查任务是否存在
   *
   * @param taskId 任务ID
   * @return 如果存在返回 {@code true}，否则 {@code false}
   */
  boolean checkExist(Long taskId);

  /**
   * 检查任务是否存在
   *
   * @param taskId 任务ID
   * @return 如果存在返回 {@code true}，否则 {@code false}
   */
  boolean checkExistAndRemove(Long taskId);

  /**
   * 获取任务并检查
   *
   * @param taskId 任务ID
   * @return {@code SystemTask}
   */
  SystemTask getTaskAndCheck(Long taskId);

  /**
   * 创建任务
   *
   * @param scheduler 调度器
   * @param systemTask 任务
   */
  void createTask(Scheduler scheduler, SystemTask systemTask);

  /**
   * 保存
   *
   * @param systemTask {@code SystemTask}
   */
  void saveTask(SystemTask systemTask);

  /**
   * 保存或更新
   *
   * @param systemTask {@code SystemTask}
   */
  void alterTask(SystemTask systemTask);

  /**
   * 删除任务
   *
   * @param taskId 任务ID
   */
  void removeTask(Long taskId);

  /**
   * 立即运行
   *
   * @param taskId 任务ID
   */
  void runTask(Long taskId);
  /**
   * 立即运行
   *
   * @param taskId 任务ID
   * @param param 参数
   */
  void runTask(Long taskId, String param);

  /**
   * 暂停运行
   *
   * @param taskId 任务ID
   */
  void pauseTask(Long taskId);

  /**
   * 恢复任务
   *
   * @param taskId 任务ID
   */
  void resumeTask(Long taskId);
}
