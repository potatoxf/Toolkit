package pxf.infrastructure.system.task;

/**
 * @author potatoxf
 * @date 2021/5/8
 */
public interface SystemTaskExecutorSupplier {

  /**
   * 获取执行者
   *
   * @return 执行者
   */
  String getExecutor();
  /**
   * 获取执行者IP地址
   *
   * @return 执行者IP地址
   */
  String getExecutorIpAddress();
}
