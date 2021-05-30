package pxf.toolkit.components.model.table;

import java.util.List;
import pxf.toolkit.components.model.AttributeMessageLineData;
import pxf.toolkit.components.model.decorator.TableDecorator;

/**
 * 表格信息
 *
 * @author potatoxf
 * @date 2021/4/1
 */
public interface TableInformation {

  /**
   * 表格标题
   *
   * @return {@code String}
   */
  String title();

  /**
   * 表格列信息
   *
   * @return {@code <T extends TableColData> List<T>}
   */
  <T extends TableColMeta> List<T> columns();

  /**
   * 表格数据
   *
   * @return {@code <T extends TableData> List<T>}
   */
  <T extends TableData> List<T> tableData();

  /**
   * 表格属性行信息
   *
   * @return {@code <T extends AttributeMessageLineData> List<T>}
   */
  <T extends AttributeMessageLineData> List<T> headData();

  /**
   * 表格属性行信息
   *
   * @return {@code <T extends AttributeMessageLineData> List<T>}
   */
  <T extends AttributeMessageLineData> List<T> tailData();

  /**
   * 最大行数
   *
   * @return {@code Integer} 最大行数，如果为 {@code null}则为默认值
   */
  Integer maxRowSize();

  /**
   * 是否开启行合并
   *
   * @return 如果开启返回 {@code true}，否则返回 {@code false}，如果为 {@code null}则为默认值
   */
  Boolean isOpenRowSpan();

  /**
   * 表格装饰器
   *
   * @return {@code <T extends TableDecorator> T}
   */
  <T extends TableDecorator> T decorator();
}
