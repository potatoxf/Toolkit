package pxf.toolkit.components.model.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * 默认表格列数据
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public class DefaultTableColMeta implements TableColMeta {

  private String[] catalogTitle;
  private TableData tableData;
  private String header;
  private Object reference;
  private boolean isSorted;
  private boolean isRowSpan;
  private boolean isLimitByPreColRowSpanRange;

  /**
   * 分类标题
   *
   * @return {@code String[]}
   */
  @Override
  public String[] catalogTitle() {
    return catalogTitle;
  }

  /**
   * 表头
   *
   * @return {@code String}
   */
  @Override
  public String header() {
    return header;
  }

  /**
   * 引用键
   *
   * @return {@code Object}
   */
  @Override
  public Object reference() {
    return reference;
  }

  /**
   * 是否按照该列排序
   *
   * @return 如果 {@code true}则按该列排序，否则不按该列排序
   */
  @Override
  public boolean isSorted() {
    return isSorted;
  }

  /**
   * 是否合并相同相邻行的数据
   *
   * @return 如果 {@code true}则合并相邻行相同的数据，否则正常输出
   */
  @Override
  public boolean isRowSpan() {
    return isRowSpan;
  }

  /**
   * 是否跟随前一列的合并范围
   *
   * @return 如果 {@code true}则合并相邻行相同的数据小于前面一列的范围，否则正常合并
   */
  @Override
  public boolean isLimitRowSpanRangeByPreviousColumn() {
    return isLimitByPreColRowSpanRange;
  }

  /**
   * 返回这一列数据迭代器
   *
   * @return {@code Iterator<Object>}
   */
  @Nonnull
  @Override
  public Iterator<Object> iterator() {
    List<Object> result = new ArrayList<>(tableData.rows());
    for (TableRowData tableDatum : tableData) {
      result.add(tableDatum.getData(reference));
    }
    return result.iterator();
  }

  /**
   * 加工数据
   *
   * @param tableRowData 当前表格行数据
   * @param input 当前数据
   * @return 返回处理后的数据
   */
  @Override
  public Object processing(TableRowData tableRowData, Object input) {
    return input;
  }

  // --------------------------------------------------------------------------- set

  public DefaultTableColMeta setCatalogTitle(String... catalogTitle) {
    this.catalogTitle = catalogTitle;
    return this;
  }

  public DefaultTableColMeta setTableData(TableData tableData) {
    this.tableData = tableData;
    return this;
  }

  public DefaultTableColMeta setHeader(String header) {
    this.header = header;
    return this;
  }

  public DefaultTableColMeta setReference(Object reference) {
    this.reference = reference;
    return this;
  }

  public DefaultTableColMeta setSorted(boolean sorted) {
    isSorted = sorted;
    return this;
  }

  public DefaultTableColMeta setRowSpan(boolean rowSpan) {
    isRowSpan = rowSpan;
    return this;
  }

  public DefaultTableColMeta setLimitByPreColRowSpanRange(boolean limitByPreColRowSpanRange) {
    isLimitByPreColRowSpanRange = limitByPreColRowSpanRange;
    return this;
  }
}
