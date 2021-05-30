package pxf.toolkit.components.model.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Map表格行
 *
 * @author potatoxf
 * @date 2021/3/31
 */
@SuppressWarnings("unchecked")
public class DefaultTableRowData implements TableRowData {

  private final Map<Object, Object> map;
  private List<Object> values;

  public DefaultTableRowData(Map map) {
    this.map = map;
  }

  /**
   * 有多少列
   *
   * @return {@code int}
   */
  @Override
  public int columns() {
    return map.size();
  }

  /**
   * 获取数据
   *
   * @param column 列
   * @return 返回数据，不存在返回 {@code null}
   */
  @Override
  public Object getData(int column) {
    initValues();
    if (column < 0 || column >= columns()) {
      return null;
    }
    return values.get(column);
  }

  @Override
  public Object getData(Object reference) {
    if (reference == null || !map.containsKey(reference)) {
      return null;
    }
    return map.get(reference);
  }

  @Nonnull
  @Override
  public Iterator<Object> iterator() {
    initValues();
    return values.iterator();
  }

  private void initValues() {
    if (values == null) {
      values = new ArrayList<>(map.values());
    }
  }
}
