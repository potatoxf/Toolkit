package pxf.toolkit.components.model.table;

import java.io.Serializable;

/**
 * 表格列元信息数据
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public interface TableColMeta extends Iterable<Object>, Serializable {

  /**
   * 分类标题
   *
   * @return {@code String[]}
   */
  String[] catalogTitle();

  /**
   * 表头
   *
   * @return {@code String}
   */
  String header();

  /**
   * 引用键
   *
   * @return {@code Object}
   */
  Object reference();

  /**
   * 是否按照该列排序
   *
   * @return 如果 {@code true}则按该列排序，否则不按该列排序
   */
  boolean isSorted();
  /**
   * 是否合并相同相邻行的数据
   *
   * @return 如果 {@code true}则合并相邻行相同的数据，否则正常输出
   */
  boolean isRowSpan();

  /**
   * 是否跟随前一列的合并范围
   *
   * @return 如果 {@code true}则合并相邻行相同的数据小于前面一列的范围，否则正常合并
   */
  boolean isLimitRowSpanRangeByPreviousColumn();

  /**
   * 加工数据
   *
   * @param tableRowData 当前表格行数据
   * @param input 当前数据
   * @return 返回处理后的数据
   */
  Object processing(TableRowData tableRowData, Object input);
}
