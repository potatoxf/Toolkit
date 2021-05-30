package pxf.infrastructure.system.task;

/**
 * 任务
 *
 * @author potatoxf
 * @date 2021/5/6
 */
public interface ISystemTask {

  /**
   * 执行任务
   *
   * @param param 参数
   * @throws Exception 如果任务执行发送异常
   */
  void execute(String param) throws Exception;
}
