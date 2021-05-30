package pxf.toolkit.components.model.table;

import java.util.ArrayList;
import java.util.List;
import pxf.toolkit.components.model.AttributeMessageLineData;
import pxf.toolkit.components.model.decorator.TableDecorator;

/**
 * 默认表格信息
 *
 * @author potatoxf
 * @date 2021/4/3
 */
@SuppressWarnings("unchecked")
public class DefaultTableInformation implements TableInformation {

  /** 标题 */
  private String title;
  /** 列信息数据 */
  private List<TableColMeta> columns;
  /** 表格数据 */
  private List<TableData> tableData;
  /** 头数据 */
  private List<AttributeMessageLineData> headData;
  /** 尾部数据 */
  private List<AttributeMessageLineData> tailData;
  /** 最大行数 */
  private Integer maxRowSize;
  /** 是否开启行合并 */
  private Boolean isOpenRowSpan;
  /** 表格装饰器 */
  private TableDecorator decorator;

  /**
   * 表格标题
   *
   * @return {@code String}
   */
  @Override
  public String title() {
    return title;
  }

  /**
   * 表格列信息
   *
   * @return {@code List<TableColumnData>}
   */
  @Override
  public <T extends TableColMeta> List<T> columns() {
    return (List<T>) columns;
  }

  /**
   * 表格数据
   *
   * @return {@code List<TableData>}
   */
  @Override
  public <T extends TableData> List<T> tableData() {
    return (List<T>) tableData;
  }

  /**
   * 表格属性行信息
   *
   * @return {@code List<AttributeMessageLineData> }
   */
  @Override
  public <T extends AttributeMessageLineData> List<T> headData() {
    return (List<T>) headData;
  }

  /**
   * 表格属性行信息
   *
   * @return {@code List<AttributeMessageLineData>}
   */
  @Override
  public <T extends AttributeMessageLineData> List<T> tailData() {
    return (List<T>) tailData;
  }

  /**
   * 最大行数
   *
   * @return {@code Integer} 最大行数，如果为 {@code null}则为默认值
   */
  @Override
  public Integer maxRowSize() {
    return maxRowSize;
  }

  /**
   * 是否开启行合并
   *
   * @return 如果开启返回 {@code true}，否则返回 {@code false}
   */
  @Override
  public Boolean isOpenRowSpan() {
    return isOpenRowSpan;
  }

  /**
   * 表格装饰器
   *
   * @return {@code <T extends TableDecorator> T}
   */
  @Override
  public <T extends TableDecorator> T decorator() {
    return null;
  }

  public <T extends TableData> DefaultTableInformation addTableData(T tableData) {
    if (this.tableData == null) {
      this.tableData = new ArrayList<>();
    }
    this.tableData.add(tableData);
    return this;
  }

  public <T extends TableColMeta> DefaultTableInformation addColumn(T tableColumn) {
    if (columns == null) {
      columns = new ArrayList<>();
    }
    columns.add(tableColumn);
    return this;
  }

  public <T extends AttributeMessageLineData> DefaultTableInformation addHeadData(
      T attributeMessageLineData) {
    if (headData == null) {
      headData = new ArrayList<>();
    }
    headData.add(attributeMessageLineData);
    return this;
  }

  public <T extends AttributeMessageLineData> DefaultTableInformation addTailData(
      T attributeMessageLineData) {
    if (tailData == null) {
      tailData = new ArrayList<>();
    }
    tailData.add(attributeMessageLineData);
    return this;
  }

  // --------------------------------------------------------------------------- get set

  public String getTitle() {
    return title;
  }

  public DefaultTableInformation setTitle(String title) {
    this.title = title;
    return this;
  }

  public List<TableColMeta> getColumns() {
    return columns;
  }

  public DefaultTableInformation setColumns(List<TableColMeta> columns) {
    this.columns = columns;
    return this;
  }

  public List<TableData> getTableData() {
    return tableData;
  }

  public DefaultTableInformation setTableData(List<TableData> tableData) {
    this.tableData = tableData;
    return this;
  }

  public List<AttributeMessageLineData> getHeadData() {
    return headData;
  }

  public DefaultTableInformation setHeadData(List<AttributeMessageLineData> headData) {
    this.headData = headData;
    return this;
  }

  public List<AttributeMessageLineData> getTailData() {
    return tailData;
  }

  public DefaultTableInformation setTailData(List<AttributeMessageLineData> tailData) {
    this.tailData = tailData;
    return this;
  }

  public Integer getMaxRowSize() {
    return maxRowSize;
  }

  public DefaultTableInformation setMaxRowSize(Integer maxRowSize) {
    this.maxRowSize = maxRowSize;
    return this;
  }

  public Boolean getOpenRowSpan() {
    return isOpenRowSpan;
  }

  public DefaultTableInformation setOpenRowSpan(Boolean openRowSpan) {
    isOpenRowSpan = openRowSpan;
    return this;
  }

  public TableDecorator getDecorator() {
    return decorator;
  }

  public DefaultTableInformation setDecorator(TableDecorator decorator) {
    this.decorator = decorator;
    return this;
  }
}
