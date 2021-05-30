package pxf.toolkit.extension.office;

import pxf.toolkit.extension.office.manager.OfficeContext;
import pxf.toolkit.extension.office.manager.OfficeException;

/**
 * Office 任务
 *
 * @author potatoxf
 * @date 2021/4/20
 */
public interface OfficeTask {

  /**
   * 执行Office任务
   *
   * @param context Office上下文环境
   * @throws OfficeException 如果Office处理出现异常
   */
  void execute(OfficeContext context) throws OfficeException;
}
