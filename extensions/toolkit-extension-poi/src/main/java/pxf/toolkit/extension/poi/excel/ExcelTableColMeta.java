package pxf.toolkit.extension.poi.excel;

import pxf.toolkit.components.model.table.TableColMeta;

/**
 * Excel表格列元信息数据
 *
 * @author potatoxf
 * @date 2021/4/5
 */
public interface ExcelTableColMeta extends TableColMeta {
  /**
   * 单元格类型
   *
   * @return 单元格类型
   */
  Integer cellType();
  /**
   * 单元格数据格式
   *
   * <p>单元格的内置数据格式
   *
   * @return 数据格式
   */
  String dataFormat();
}
