package pxf.toolkit.extension.poi.excel.builder;

import pxf.toolkit.basic.function.Exporter;
import pxf.toolkit.basic.function.ExporterBuilder;
import pxf.toolkit.components.model.table.DefaultTableInformation;
import pxf.toolkit.extension.poi.excel.ExcelExporterType;
import pxf.toolkit.extension.poi.excel.ExcelTableInformation;
import pxf.toolkit.extension.poi.excel.decorator.ExcelTableDecorator;

/**
 * @author potatoxf
 * @date 2021/4/5
 */
public class BeanListExcelExportBuilder extends DefaultTableInformation
    implements ExcelTableInformation, ExporterBuilder {

  /**
   * 构建导出器
   *
   * @return {@code Exporter}
   */
  @Override
  public Exporter build() {
    return null;
  }

  /**
   * 文件格式
   *
   * @return {@code xls} {@code xlsx}
   */
  @Override
  public ExcelExporterType fileFormat() {
    return null;
  }

  /**
   * sheet名称
   *
   * @return {@code String}
   */
  @Override
  public String sheetName() {
    return null;
  }

  /**
   * 最大sheet数量
   *
   * @return {@code Integer} 最大sheet数量，如果为 {@code null}则为默认值
   */
  @Override
  public Integer maxSheetSize() {
    return null;
  }

  /**
   * 表格装饰器
   *
   * @return {@code ExcelTableDecorator}
   */
  @Override
  public ExcelTableDecorator decorator() {
    return null;
  }
}
