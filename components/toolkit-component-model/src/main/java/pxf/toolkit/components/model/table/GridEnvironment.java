package pxf.toolkit.components.model.table;

/**
 * 格式所在环境
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public class GridEnvironment {

  /** 表格列信息 */
  private final TableColMeta tableColMeta;
  /** 表格行数据 */
  private final TableRowData tableRowData;
  /** 当前数据 */
  private final Object data;
  /** 当前行 */
  private final int row;
  /** 当前列 */
  private final int col;

  public GridEnvironment(
      TableColMeta tableColMeta, TableRowData tableRowData, Object data, int row, int col) {
    this.tableColMeta = tableColMeta;
    this.tableRowData = tableRowData;
    this.data = data;
    this.row = row;
    this.col = col;
  }

  public TableColMeta getTableColumnData() {
    return tableColMeta;
  }

  public TableRowData getTableRowData() {
    return tableRowData;
  }

  public Object getData() {
    return data;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
}
