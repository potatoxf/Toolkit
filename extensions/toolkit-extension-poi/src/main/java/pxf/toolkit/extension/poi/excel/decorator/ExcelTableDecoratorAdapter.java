package pxf.toolkit.extension.poi.excel.decorator;

import pxf.toolkit.components.model.decorator.TableDecoratorAdapter;
import pxf.toolkit.components.model.table.GridEnvironment;
import pxf.toolkit.extension.poi.excel.ExcelCellType;

/**
 * Excel表格装饰适配器
 *
 * @author potatoxf
 * @date 2021/4/3
 */
public class ExcelTableDecoratorAdapter extends TableDecoratorAdapter
    implements ExcelTableDecorator {

  @Override
  public Boolean handleLocked(GridEnvironment gridEnvironment) {
    return null;
  }

  @Override
  public Boolean handleHidden(GridEnvironment gridEnvironment) {
    return null;
  }

  @Override
  public String handleFormula(GridEnvironment gridEnvironment) {
    return null;
  }

  @Override
  public ExcelCellType handleCellType(GridEnvironment gridEnvironment) {
    return null;
  }

  @Override
  public String handleDataFormat(GridEnvironment gridEnvironment) {
    return null;
  }

  @Override
  public ExcelDecoratorStyle handleFillPattern(GridEnvironment gridEnvironment) {
    return null;
  }
}
