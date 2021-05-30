package pxf.infrastructure.system.basic.dbentity;

/**
 * 操作人员提供器
 *
 * @author potatoxf
 * @date 2021/5/14
 */
@FunctionalInterface
public interface OperatorSupplier {

  /**
   * 获取创建者
   *
   * @return {@code String}
   */
  String getOperator();
}
