package pxf.toolkit.components.model.table;

import java.io.Serializable;
import java.util.Collection;

/**
 * 表格
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public interface TableData extends Iterable<TableRowData>, Serializable {

  /**
   * 总共行
   *
   * @return {@code int}
   */
  int rows();

  /**
   * 获取表格行
   *
   * @param row 行号
   * @return {@code TableRow}
   */
  TableRowData get(int row);

  /**
   * 指定要排序的列数据进行排序
   *
   * @param references 列引用集合
   */
  void sort(Object... references);

  /**
   * 指定要排序的列数据进行排序
   *
   * @param references 列引用集合
   */
  void sort(Collection<Object> references);
}
