package pxf.toolkit.components.model.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 默认表格数据
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public class DefaultTableData implements TableData {

  /** 表格行 */
  private final List<TableRowData> tableRowData;

  protected DefaultTableData(List<TableRowData> tableRowData) {
    this.tableRowData = tableRowData;
  }

  public static DefaultTableData of(List<Map> mapList) {
    List<TableRowData> tableRowData = new ArrayList<>(mapList.size());
    for (Map map : mapList) {
      tableRowData.add(new DefaultTableRowData(map));
    }
    return new DefaultTableData(tableRowData);
  }

  public static DefaultTableData ofInstance(List<TableRowData> tableRowData) {
    return new DefaultTableData(tableRowData);
  }

  /**
   * 总共行
   *
   * @return {@code int}
   */
  @Override
  public int rows() {
    return tableRowData.size();
  }

  /**
   * 获取表格行
   *
   * @param row 行号
   * @return {@code TableRow}
   */
  @Override
  public TableRowData get(int row) {
    return tableRowData.get(row);
  }

  /**
   * 排序列
   *
   * @param references 引用集合
   */
  @Override
  public void sort(Object... references) {
    for (Object reference : references) {
      tableRowData.sort(new RowDataComparator(reference));
    }
  }

  /**
   * 排序列
   *
   * @param references 引用集合
   */
  @Override
  public void sort(Collection<Object> references) {
    for (Object reference : references) {
      tableRowData.sort(new RowDataComparator(reference));
    }
  }

  /**
   * Returns an iterator over elements of type {@code T}.
   *
   * @return an Iterator.
   */
  @Override
  public Iterator<TableRowData> iterator() {
    return tableRowData.iterator();
  }

  private static class RowDataComparator implements Comparator<TableRowData> {

    private final Object key;
    private final boolean isNullLast;

    public RowDataComparator(Object key) {
      this(key, true);
    }

    public RowDataComparator(Object key, boolean isNullLast) {
      this.key = key;
      this.isNullLast = isNullLast;
    }

    @Override
    public int compare(TableRowData o1, TableRowData o2) {
      Object v1 = o1.getData(key);
      Object v2 = o2.getData(key);
      if (v1 == null && v2 == null) {
        return 0;
      }
      if (v1 == null) {
        return isNullLast ? 1 : -1;
      }
      if (v2 == null) {
        return isNullLast ? -1 : 1;
      }
      Class<?> c1 = v1.getClass();
      Class<?> c2 = v2.getClass();
      if (v1 instanceof Comparable && v2 instanceof Comparable) {
        if (c1.equals(c2)) {
          return ((Comparable) v1).compareTo(v2);
        }
        if (c1.isAssignableFrom(c2)) {
          return ((Comparable) v1).compareTo(v2);
        }
        if (c2.isAssignableFrom(c1)) {
          return -((Comparable) v2).compareTo(v1);
        }
        throw new IllegalArgumentException(
            String.format(
                "The key [%s] association value type does not match for [%s] and [%s]",
                key, c1, c2));
      }
      throw new IllegalArgumentException(
          String.format(
              "The key [%s] association value type does not implements comparator for [%s] and [%s]",
              key, c1, c2));
    }
  }
}
