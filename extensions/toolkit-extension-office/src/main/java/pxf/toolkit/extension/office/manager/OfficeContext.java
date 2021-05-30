package pxf.toolkit.extension.office.manager;
/**
 * @author potatoxf
 * @date 2021/4/20
 */
public interface OfficeContext {

  /**
   * 获取服务
   *
   * @param serviceName 服务名
   * @return 某个接口 {@code UnoRuntime.queryInterface(type,obj)}
   */
  Object getService(String serviceName);
}
