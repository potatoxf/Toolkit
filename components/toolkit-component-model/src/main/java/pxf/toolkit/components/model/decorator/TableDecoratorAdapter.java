package pxf.toolkit.components.model.decorator;

import pxf.toolkit.components.model.table.GridEnvironment;

/**
 * 表格装饰器适配器
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public class TableDecoratorAdapter implements TableDecorator {

  /**
   * 放缩比例
   *
   * @return 如果为 {@code null}，默认为 {@code 1}
   */
  @Override
  public Double scale() {
    return null;
  }

  /**
   * 最小行高
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  @Override
  public Number minRowHeight() {
    return null;
  }

  /**
   * 最大行高
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  @Override
  public Number maxRowHeight() {
    return null;
  }

  /**
   * 最小列宽
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  @Override
  public Number minColWidth() {
    return null;
  }

  /**
   * 最大列宽
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  @Override
  public Number maxColWidth() {
    return null;
  }

  /**
   * 处理当前宽度
   *
   * @param gridEnvironment 单元格所在的环境
   * @param oldWidth 之前宽度
   * @return 返回处理后的宽度，返回 {@code null}则按默认
   */
  @Override
  public Integer handleWidth(GridEnvironment gridEnvironment, int oldWidth) {
    return null;
  }

  /**
   * 处理当前高度
   *
   * @param gridEnvironment 单元格所在的环境
   * @param oldHeight 之前高度
   * @return 返回处理后的高度，返回 {@code null}则按默认
   */
  @Override
  public Integer handleHeight(GridEnvironment gridEnvironment, int oldHeight) {
    return null;
  }

  /**
   * 处理当前背景颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的背景颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleBackgroundColor(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理当前前景颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的前景颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleForegroundColor(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理当前字体样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据，返回 {@code null}则按默认
   */
  @Override
  public String handleFontName(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理当前字体高度
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据，返回 {@code null}则按默认
   */
  @Override
  public Integer handleFontHeight(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理当前字体颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleFontColor(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理当前字体是否斜体
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据是否斜体，返回 {@code null}则按默认
   */
  @Override
  public Boolean handleFontItalic(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理当前字体粗细
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体粗细，返回 {@code null}则按默认
   */
  @Override
  public Boolean handleFontBold(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理当前是否加删除线
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据是否加删除线，返回 {@code null}则按默认
   */
  @Override
  public Boolean handleFontStrikeout(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的数据的位置
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据的位置
   */
  @Override
  public FontStyle handleFontTypeOffset(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的数据的字符集
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据的字符集
   */
  @Override
  public FontStyle handleFontCharSet(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的是否包裹文本
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的是否包裹文本，返回 {@code null}则按默认
   */
  @Override
  public Boolean handleWrapText(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的间隔
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的间隔，返回 {@code null}则按默认
   */
  @Override
  public Integer handleIndention(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的旋转角度
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的旋转角度，返回 {@code null}则按默认
   */
  @Override
  public Integer handleRotation(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleBottomBorderColor(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleTopBorderColor(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleRightBorderColor(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleLeftBorderColor(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的下划线
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的下划线，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleUnderline(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleBorderBottom(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleBorderTop(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleBorderRight(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleBorderLeft(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的垂直对齐方式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的垂直对齐方式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleVerticalAlignment(GridEnvironment gridEnvironment) {
    return null;
  }

  /**
   * 处理后的水平对齐方式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的水平对齐方式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleHorizontalAlignment(GridEnvironment gridEnvironment) {
    return null;
  }
}
