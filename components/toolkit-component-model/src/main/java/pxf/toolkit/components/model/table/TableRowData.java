package pxf.toolkit.components.model.table;

import java.io.Serializable;

/**
 * 表格行
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public interface TableRowData extends Iterable<Object>, Serializable {

  /**
   * 有多少列
   *
   * @return {@code int}
   */
  int columns();

  /**
   * 获取数据
   *
   * @param column 列
   * @return 返回数据，不存在返回 {@code null}
   */
  Object getData(int column);

  /**
   * 获取数据
   *
   * @param reference 引用键
   * @return 返回数据，不存在返回 {@code null}
   */
  Object getData(Object reference);
}
