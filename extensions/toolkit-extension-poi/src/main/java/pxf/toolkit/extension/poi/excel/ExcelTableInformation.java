package pxf.toolkit.extension.poi.excel;

import pxf.toolkit.components.model.table.TableInformation;
import pxf.toolkit.extension.poi.excel.decorator.ExcelTableDecorator;

/**
 * Excel表信息
 *
 * @author potatoxf
 * @date 2021/4/3
 */
public interface ExcelTableInformation extends TableInformation {

  /**
   * 文件格式
   *
   * @return {@code xls} {@code xlsx}
   */
  ExcelExporterType fileFormat();

  /**
   * sheet名称
   *
   * @return {@code String}
   */
  String sheetName();

  /**
   * 最大sheet数量
   *
   * @return {@code Integer} 最大sheet数量，如果为 {@code null}则为默认值
   */
  Integer maxSheetSize();

  /**
   * 表格装饰器
   *
   * @return {@code ExcelTableDecorator}
   */
  @Override
  ExcelTableDecorator decorator();
}
