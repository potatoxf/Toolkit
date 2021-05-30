package pxf.toolkit.extension.poi.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.annotation.Nonnull;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pxf.toolkit.basic.function.Exporter;
import pxf.toolkit.basic.util.Calculate;
import pxf.toolkit.basic.util.Cast;
import pxf.toolkit.basic.util.Empty;

/**
 * 抽象Excel导出
 *
 * <p>该列定义了Excel导出的框架性配置
 *
 * @author potatoxf
 * @date 2021/5/15
 */
public abstract class AbstractExcelExporter implements Exporter {

  /** 最小单元格宽度 */
  public static final String MIN_CELL_WIDTH_KEY = "minCellWidth";
  /** 最小单元格宽度 */
  private static final int CELL_MIN_WIDTH = 5;
  /** 最大单元格宽度 */
  public static final String MAX_CELL_WIDTH_KEY = "maxCellWidth";
  /** 最大单元格宽度 */
  private static final int CELL_MAX_WIDTH = 20;
  /** 行最小高度 */
  public static final String MIN_ROW_HEIGHT_KEK = "minRowHeight";
  /** 行最小高度 */
  private static final int ROW_MIN_HEIGHT = 15;
  /** 最大sheet数量 */
  public static final String MAX_SHEET_SIZE_KEY = "maxSheetSize";
  /** 最大sheet数量 */
  private static final int MAX_SHEET_SIZE = 10;
  /** 放缩比例 */
  public static final String SCALE_KEY = "scale";
  /** 放缩比例 */
  private static final double SCALE = 1d;
  /** 最大行数 */
  public static final String MAX_ROW_SIZE_KEY = "maxRowSize";
  /** 预留行数 */
  private static final int RESERVED_ROW = 0x1000;
  /** XLS最大行数 */
  private static final int XLS_MAX_ROW = 0xFFFF - RESERVED_ROW;
  /** XLSX最大行数 */
  private static final int XLSX_MAX_ROW = 0x100000 - RESERVED_ROW;
  /** 默认sheet名称 */
  public static final String DEFAULT_SHEET_NAME_KEY = "sheet";
  /** 默认sheet名称 */
  private static final String DEFAULT_SHEET_NAME = "sheet";
  /** 字符宽度因子 */
  private static final int CHAR_WIDTH_COEFFICIENT = 255;
  /** 字符宽度偏移量 */
  private static final int CHAR_WIDTH_OFFSET = 185;
  /** Excel导出类型 */
  private final ExcelExporterType excelExporterType;
  /** 是否执行过 */
  private boolean executed = false;
  /** 保存的workbook */
  private Workbook workbook;
  /** sheet名称 */
  private String sheetName;
  /** 最小单元格宽度 */
  private int minCellWidth;
  /** 最大单元格宽度 */
  private int maxCellWidth;
  /** 最小行高度 */
  private int minRowHeight;
  /** 最大sheet数量 */
  private int maxSheetSize;
  /** 最大最大行数 */
  private int maxRowSize;
  /** 放缩比例 */
  private double scale;

  protected AbstractExcelExporter(ExcelExporterType excelExporterType) {
    this.excelExporterType = excelExporterType;
  }

  /**
   * 执行导入，导入到指定输出流
   *
   * @param outputStream 输出流
   * @param parameter 参数
   * @throws IOException 如果导入发生错误
   */
  @Override
  public final void execute(OutputStream outputStream, Properties parameter) throws IOException {
    if (outputStream == null) {
      throw new NullPointerException("The outputStream must be no null");
    }
    if (!executed) {
      initConfig(parameter);
      doExecute();
      executed = true;
    }
    workbook.write(outputStream);
  }

  /**
   * 初始化导出配置
   *
   * @param parameter 参数
   */
  private void initConfig(Properties parameter) {
    Properties properties = new Properties();
    preInitConfig(properties, parameter);
    properties.putAll(parameter);
    minCellWidth = Cast.intValue(properties.get(MIN_CELL_WIDTH_KEY), CELL_MIN_WIDTH);
    maxCellWidth = Cast.intValue(properties.get(MAX_CELL_WIDTH_KEY), CELL_MAX_WIDTH);
    minRowHeight = Cast.intValue(properties.get(MIN_ROW_HEIGHT_KEK), ROW_MIN_HEIGHT);
    scale = Cast.doubleValue(properties.get(MAX_SHEET_SIZE_KEY), MAX_SHEET_SIZE);

    sheetName = Cast.stringValue(properties.get(DEFAULT_SHEET_NAME_KEY), DEFAULT_SHEET_NAME);
    maxSheetSize = Cast.intValue(properties.get(MAX_SHEET_SIZE_KEY), MAX_SHEET_SIZE);
    maxRowSize =
        Cast.intValue(
            properties.get(MAX_ROW_SIZE_KEY),
            excelExporterType == null || excelExporterType == ExcelExporterType.XLS
                ? XLS_MAX_ROW
                : XLSX_MAX_ROW);
  }

  /**
   * 预初始化参数
   *
   * @param builtInParameters 内置参数
   * @param realTimeParameters 实时参数
   */
  protected abstract void preInitConfig(
      Properties builtInParameters, Properties realTimeParameters);

  /**
   * 执行导出
   *
   * <p>执行Excel赋值操作
   */
  protected abstract void doExecute();

  /**
   * 获取工作簿
   *
   * @return {@code Workbook}
   */
  @Nonnull
  protected Workbook getWorkbook() {
    if (workbook == null) {
      workbook = createWorkbook();
    }
    return workbook;
  }

  /**
   * 创建工作簿
   *
   * @return {@code Workbook}
   */
  protected Workbook createWorkbook() {
    switch (excelExporterType) {
      case XLS:
        return new HSSFWorkbook();
      case XLSX:
        return new XSSFWorkbook();
      default:
        return new SXSSFWorkbook();
    }
  }

  /**
   * 获取数据值字符串
   *
   * @param cell 单元格
   * @param defaultValue 默认值
   * @return 返回数据值字符串
   */
  protected String getDataStringValue(Cell cell, String defaultValue) {
    return String.valueOf(getDataValue(cell, defaultValue));
  }

  /**
   * 获取数据值
   *
   * @param cell 单元格
   * @param defaultValue 默认值
   * @return 返回数据值
   */
  protected Object getDataValue(Cell cell, Object defaultValue) {
    if (cell != null) {
      CellType cellType = cell.getCellType();
      switch (cellType) {
        case _NONE:
          return Empty.STRING;
        case ERROR:
          return cell.getErrorCellValue();
        case BOOLEAN:
          return cell.getBooleanCellValue();
        case FORMULA:
        case NUMERIC:
          if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
          }
          return cell.getNumericCellValue();
        default:
          return cell.getRichStringCellValue().getString();
      }
    }
    return defaultValue;
  }

  /**
   * 计算单元格宽度
   *
   * @param content 内容
   * @return 返回单元格宽度
   */
  protected int calculateCellWidth(Object content) {
    return calculateCellWidth(String.valueOf(content));
  }

  /**
   * 计算单元格宽度
   *
   * @param content 内容
   * @return 返回单元格宽度
   */
  protected int calculateCellWidth(String content) {
    int charWidth = calculateCharWidth(content);
    int minCellWidth = getMinCellWidth();
    if (charWidth < minCellWidth) {
      return minCellWidth;
    }
    int maxCellWidth = getMaxCellWidth();
    return Math.min(charWidth, maxCellWidth);
  }

  /**
   * 计算字符串宽度
   *
   * @param input 字符个数
   * @return 返回字符宽度
   */
  protected int calculateCharWidth(String input) {
    return (int) Calculate.charLength(input, 1, 2, 0);
  }

  /**
   * 计算字符宽度
   *
   * @param charSize 字符个数
   * @return 返回字符宽度
   */
  protected int calculateWidth(int charSize) {
    return (int) (scale * CHAR_WIDTH_COEFFICIENT * charSize + CHAR_WIDTH_OFFSET);
  }

  // ---------------------------------------------------------------------------

  protected final ExcelExporterType getExcelExporterType() {
    return excelExporterType;
  }

  protected final String getSheetName() {
    return sheetName;
  }

  protected final int getMinCellWidth() {
    return this.minCellWidth;
  }

  protected final int getMaxCellWidth() {
    return this.maxCellWidth;
  }

  protected final int getMinRowHeight() {
    return this.minRowHeight;
  }

  protected final int getMaxSheetSize() {
    return this.maxSheetSize;
  }

  protected final int getMaxRowSize() {
    return this.maxRowSize;
  }

  protected final double getScale() {
    return this.scale;
  }
}
