package pxf.toolkit.extension.poi.excel.decorator;

import pxf.toolkit.components.model.decorator.TableDecorator;
import pxf.toolkit.components.model.table.GridEnvironment;
import pxf.toolkit.extension.poi.excel.ExcelCellType;

/**
 * Excel表格装饰器
 *
 * @author potatoxf
 * @date 2021/4/3
 */
public interface ExcelTableDecorator extends TableDecorator {

  /**
   * 处理后的是否锁定
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的是否锁定，返回 {@code null}则按默认
   */
  Boolean handleLocked(GridEnvironment gridEnvironment);

  /**
   * 处理后的是否隐藏
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的是否隐藏，返回 {@code null}则按默认
   */
  Boolean handleHidden(GridEnvironment gridEnvironment);

  /**
   * 处理后的单元格类型
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据公式
   */
  String handleFormula(GridEnvironment gridEnvironment);

  /**
   * 处理后的单元格类型
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的单元格类型
   */
  ExcelCellType handleCellType(GridEnvironment gridEnvironment);

  /**
   * 处理后的单元格数据格式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据格式
   */
  String handleDataFormat(GridEnvironment gridEnvironment);

  /**
   * 处理后的填充模式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的填充模式
   */
  ExcelDecoratorStyle handleFillPattern(GridEnvironment gridEnvironment);
}
