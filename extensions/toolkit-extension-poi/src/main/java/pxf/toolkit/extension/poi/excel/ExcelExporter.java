package pxf.toolkit.extension.poi.excel;

import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pxf.toolkit.basic.lang.collection.GroupConditionalMap;
import pxf.toolkit.basic.util.Is;
import pxf.toolkit.basic.util.Valid;
import pxf.toolkit.components.model.AttributeMessageData;
import pxf.toolkit.components.model.AttributeMessageLineData;
import pxf.toolkit.components.model.table.TableColMeta;
import pxf.toolkit.components.model.table.TableData;
import pxf.toolkit.components.model.table.TableRowData;
import pxf.toolkit.extension.poi.excel.decorator.ExcelTableDecorator;
import pxf.toolkit.extension.poi.excel.decorator.ExcelTableDecoratorAdapter;

/**
 * Excel导出器
 *
 * @author potatoxf
 * @date 2021/5/15
 */
public class ExcelExporter extends AbstractExcelExporter {

  /** 列宽度统计器 */
  private final GroupConditionalMap<Integer, Integer> widthStatics =
      new GroupConditionalMap<>((o, n) -> n > o);
  /** Excel表信息 */
  private final ExcelTableInformation excelTableInformation;
  /** 表格数据 */
  private List<TableData> tableData;
  /** 总数据大小 */
  private int totalDataSize = -1;
  /** 头数据行大小 */
  private int headDataRowSize = -1;
  /** 尾数据行大小 */
  private int tailDataRowSize = -1;
  /** 分类 */
  private String[][] classifyTitle;
  /** 当前工作簿 */
  private Sheet sheet = null;
  /** sheet指针 */
  private int sp = 0;
  /** 行指针 */
  private int rp = 0;
  /** 单页数据指针 */
  private int pdp = 0;
  /** 数据指针 */
  private int dp = 0;

  protected ExcelExporter(ExcelTableInformation excelTableInformation) {
    super(excelTableInformation.fileFormat());
    this.excelTableInformation = excelTableInformation;
    this.tableData = excelTableInformation.tableData();
  }

  /**
   * 预初始化参数
   *
   * @param builtInParameters 内置参数
   * @param realTimeParameters 实时参数
   */
  @Override
  protected void preInitConfig(Properties builtInParameters, Properties realTimeParameters) {
    ExcelTableDecorator excelTableDecorator = getExcelTableDecorator();
    builtInParameters.put(MIN_CELL_WIDTH_KEY, excelTableDecorator.minColWidth());
    builtInParameters.put(MAX_CELL_WIDTH_KEY, excelTableDecorator.maxColWidth());
    builtInParameters.put(MIN_ROW_HEIGHT_KEK, excelTableDecorator.maxRowHeight());
    builtInParameters.put(SCALE_KEY, excelTableDecorator.scale());

    builtInParameters.put(DEFAULT_SHEET_NAME_KEY, excelTableInformation.sheetName());
    builtInParameters.put(MAX_SHEET_SIZE_KEY, excelTableInformation.maxSheetSize());
    builtInParameters.put(MAX_ROW_SIZE_KEY, excelTableInformation.maxRowSize());
  }

  /** 执行导出 */
  @Override
  protected void doExecute() {
    // 最大sheet数量
    int mss = getMaxSheetSize();
    // 总共数据大小
    int tds = getTotalDataSize();
    // 最大数据大小
    int mds = getMaxDataSize();
    // 最大行数
    int mrs = getMaxRowSize();
    // mds <= mrs <= tds
    while (true) {
      fillTitle();
      fillHead();
      fillData();
      fillTail();
      // 如果数据已经完全弄完
      if (dp >= tds) {
        break;
      }
      // 切换sheet
      handleSheet();
    }
  }

  private void fillTitle() {
    String title = getTitle();
    if (Is.noEmpty(title)) {
      Sheet currentSheet = getCurrentSheet();
      Row row = currentSheet.getRow(rp++);
      Cell cell = row.createCell(0);
      cell.setCellValue(title);
    }
  }

  private void fillHead() {
    List<AttributeMessageLineData> headData = getHeadData();
    if (Is.noEmpty(headData)) {
      Sheet currentSheet = getCurrentSheet();
      for (AttributeMessageLineData headDatum : headData) {
        createEmptyRow((int) headDatum.topMargin());
        Row row = currentSheet.createRow(rp++);
        createAttributeMessageRow(headDatum.leftMargin(), headDatum.lineContents());
        createEmptyRow((int) headDatum.bottomMargin());
      }
    }
  }

  private void fillData() {}

  private TableRowData getTableRowData() {
    List<TableData> data = getTableData();
    int dataPoint = dp++;
    for (TableData tableData : data) {
      if (dataPoint < tableData.rows()) {
        return tableData.get(dataPoint);
      } else {
        dataPoint -= tableData.rows();
      }
    }
    throw new RuntimeException("Data pointer error");
  }

  /**
   * 获取表格数据
   *
   * @return {@code List<TableData>}
   */
  @Nonnull
  private List<TableData> getTableData() {
    if (tableData != null) {
      return tableData;
    }
    List<TableData> data = excelTableInformation.tableData();
    if (Is.empty(data)) {
      tableData = Collections.emptyList();
    } else {
      List<TableColMeta> columns = getColumns();
      // 找到要排序的列
      List<Object> sortedColumns =
          columns.stream()
              .filter(TableColMeta::isSorted)
              .map(TableColMeta::reference)
              .collect(Collectors.toList());
      // 排序
      data.forEach(e -> e.sort(sortedColumns));
      tableData = data;
    }
    return tableData;
  }

  private void fillTail() {
    List<AttributeMessageLineData> tailData = getTailData();
    if (Is.noEmpty(tailData)) {
      Sheet currentSheet = getCurrentSheet();
      for (AttributeMessageLineData tailDatum : tailData) {
        createEmptyRow((int) tailDatum.topMargin());
        Row row = currentSheet.createRow(rp++);
        createAttributeMessageRow(tailDatum.leftMargin(), tailDatum.lineContents());
        createEmptyRow((int) tailDatum.bottomMargin());
      }
    }
  }

  private void handleSheet() {}

  private void createAttributeMessageRow(
      double[] leftMargin, AttributeMessageData[] attributeMessageData) {
    if (Is.empty(attributeMessageData)) {
      return;
    }
    if (leftMargin.length < attributeMessageData.length) {
      double[] temp = new double[attributeMessageData.length];
      System.arraycopy(leftMargin, 0, temp, 0, leftMargin.length);
      leftMargin = temp;
    }
    Sheet currentSheet = getCurrentSheet();
    Row row = currentSheet.createRow(rp++);
    int col = 0;
    for (int i = 0; i < attributeMessageData.length; i++) {
      int v = (int) leftMargin[i];
      for (int j = 0; j < v; j++) {
        Cell cell = row.createCell(col++);
      }
      Cell attributeCell = row.createCell(col++);
      attributeCell.setCellValue(Valid.string(attributeMessageData[i].attribute()));
      Cell messageCell = row.createCell(col++);
      messageCell.setCellValue(Valid.string(attributeMessageData[i].message()));
    }
  }

  private void createEmptyRow(int totalRow) {
    if (totalRow <= 0) {
      return;
    }
    Sheet currentSheet = getCurrentSheet();
    for (int i = 0; i < totalRow; i++) {
      currentSheet.createRow(rp++);
    }
  }

  /**
   * 获取当前sheet
   *
   * @return {@code Sheet}
   */
  @Nonnull
  private Sheet getCurrentSheet() {
    if (sheet == null) {
      Workbook workbook = getWorkbook();
      String currentSheetName = getCurrentSheetName();
      sheet = workbook.createSheet(currentSheetName);
    }
    return sheet;
  }

  /**
   * 获取当前sheet名称
   *
   * @return 返回当前当前sheet名称
   */
  @Nonnull
  private String getCurrentSheetName() {
    String sheetName = getSheetName();
    if (sp <= 0) {
      return sheetName;
    }
    return sheetName + sp;
  }

  /**
   * 获取最大数据大小
   *
   * @return {@code int}
   */
  private int getMaxDataSize() {
    int maxRow = getMaxRowSize();
    int totalDataSize = getTotalDataSize();
    String title = getTitle();
    int headDataRowSize = getHeadDataRowSize();
    int classifyTitleRowSize = getClassifyTitleRowSize();
    int tailDataRowSize = getTailDataRowSize();
    int otherRowSize =
        +headDataRowSize + classifyTitleRowSize + tailDataRowSize + (title == null ? 0 : 1);
    int realRowSize = maxRow - otherRowSize;
    if (totalDataSize <= realRowSize) {
      return totalDataSize;
    }
    boolean b = false;
    for (int i = sp + 1; i > 0; i--) {
      if (b && totalDataSize > i * realRowSize) {
        return totalDataSize - i * realRowSize;
      }
      if (totalDataSize <= i * realRowSize) {
        b = true;
      } else {
        return realRowSize;
      }
    }
    return totalDataSize;
  }

  /**
   * 获取标题
   *
   * @return 返回标题
   */
  @Nullable
  private String getTitle() {
    return excelTableInformation.title();
  }

  /**
   * 获取头部信息行数据
   *
   * @return {@code List<AttributeMessageLineData>}
   */
  private int getHeadDataRowSize() {
    if (headDataRowSize < 0) {
      List<AttributeMessageLineData> headData = getHeadData();
      if (headData == null) {
        headDataRowSize = 0;
      } else {
        headDataRowSize = sumAttributeMessageRowSize(headData);
      }
    }
    return headDataRowSize;
  }

  /**
   * 获取头部信息行数据
   *
   * @return {@code List<AttributeMessageLineData>}
   */
  @Nullable
  private List<AttributeMessageLineData> getHeadData() {
    return excelTableInformation.headData();
  }
  /**
   * 获取尾部信息行数据
   *
   * @return {@code List<AttributeMessageLineData>}
   */
  private int getTailDataRowSize() {
    if (tailDataRowSize < 0) {
      List<AttributeMessageLineData> tailData = getTailData();
      if (tailData == null) {
        tailDataRowSize = 0;
      } else {
        tailDataRowSize = sumAttributeMessageRowSize(tailData);
      }
    }
    return tailDataRowSize;
  }

  /**
   * 统计属性信息行大小
   *
   * @param attributeMessageLineData 属性信息行数据
   * @return 返回占用行大小
   */
  private int sumAttributeMessageRowSize(
      @Nonnull List<AttributeMessageLineData> attributeMessageLineData) {
    return (int)
        attributeMessageLineData.stream()
            .mapToDouble(e -> e.topMargin() + e.bottomMargin() + 1)
            .sum();
  }

  /**
   * 获取尾部信息行数据
   *
   * @return {@code List<AttributeMessageLineData>}
   */
  @Nullable
  private List<AttributeMessageLineData> getTailData() {
    return excelTableInformation.tailData();
  }

  /**
   * 获取总数据大小
   *
   * @return 返回总数据量
   */
  private int getTotalDataSize() {
    if (totalDataSize < 0) {
      totalDataSize = tableData.stream().mapToInt(TableData::rows).sum();
    }
    return totalDataSize;
  }

  /**
   * 获取有多少列
   *
   * @return 返回总列数
   */
  private int getTotalColSize() {
    return tableData.size();
  }

  /**
   * 获取分类标题占用的行大小
   *
   * @return 分类标题占用的行大小
   */
  private int getClassifyTitleRowSize() {
    String[][] classifyTitle = getClassifyTitle();
    if (classifyTitle == null) {
      return 0;
    }
    return classifyTitle.length;
  }

  /**
   * 获取分类标题
   *
   * @return {@code String[][]}
   */
  @Nullable
  private String[][] getClassifyTitle() {
    if (classifyTitle == null) {
      List<TableColMeta> columns = excelTableInformation.columns();
      OptionalInt max =
          columns.stream()
              .map(TableColMeta::catalogTitle)
              .filter(Is::noEmpty)
              .mapToInt(e -> e.length)
              .max();
      if (max.isPresent()) {
        int col = columns.size();
        int row = max.getAsInt();
        String[][] temp = new String[row][col];
        for (int c = 0; c < col; c++) {
          TableColMeta tableColMeta = columns.get(c);
          String[] catalogTitle = tableColMeta.catalogTitle();
          if (Is.empty(catalogTitle)) {
            continue;
          }
          for (int r = 0; r < row; r++) {
            if (r < catalogTitle.length) {
              temp[r][c] = catalogTitle[r];
            }
          }
        }
        classifyTitle = temp;
      }
    }
    return classifyTitle;
  }

  /**
   * 获取表格列信息
   *
   * @return {@code List<TableColMeta>}
   */
  @Nonnull
  private List<TableColMeta> getColumns() {
    return excelTableInformation.columns();
  }

  @Nonnull
  private ExcelTableDecorator getExcelTableDecorator() {
    ExcelTableDecorator decorator = excelTableInformation.decorator();
    if (decorator == null) {
      return new ExcelTableDecoratorAdapter();
    }
    return decorator;
  }
}
