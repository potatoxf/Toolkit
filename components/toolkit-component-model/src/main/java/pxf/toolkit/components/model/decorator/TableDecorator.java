package pxf.toolkit.components.model.decorator;

import pxf.toolkit.components.model.table.GridEnvironment;

/**
 * 表格装饰器
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public interface TableDecorator {

  /**
   * 放缩比例
   *
   * @return 如果为 {@code null}，默认为 {@code 1}
   */
  Double scale();

  /**
   * 最小行高
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  Number minRowHeight();

  /**
   * 最大行高
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  Number maxRowHeight();

  /**
   * 最小列宽
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  Number minColWidth();

  /**
   * 最大列宽
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  Number maxColWidth();

  /**
   * 处理当前宽度
   *
   * @param gridEnvironment 单元格所在的环境
   * @param oldWidth 之前宽度
   * @return 返回处理后的宽度，返回 {@code null}则按默认
   */
  Integer handleWidth(GridEnvironment gridEnvironment, int oldWidth);

  /**
   * 处理当前高度
   *
   * @param gridEnvironment 单元格所在的环境
   * @param oldHeight 之前高度
   * @return 返回处理后的高度，返回 {@code null}则按默认
   */
  Integer handleHeight(GridEnvironment gridEnvironment, int oldHeight);

  /**
   * 处理当前背景颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的背景颜色，返回 {@code null}则按默认
   */
  Integer handleBackgroundColor(GridEnvironment gridEnvironment);

  /**
   * 处理当前前景颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的前景颜色，返回 {@code null}则按默认
   */
  Integer handleForegroundColor(GridEnvironment gridEnvironment);

  /**
   * 处理当前字体样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体，返回 {@code null}则按默认
   */
  String handleFontName(GridEnvironment gridEnvironment);

  /**
   * 处理当前字体高度
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体高度，返回 {@code null}则按默认
   */
  Integer handleFontHeight(GridEnvironment gridEnvironment);

  /**
   * 处理当前字体颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体颜色，返回 {@code null}则按默认
   */
  Integer handleFontColor(GridEnvironment gridEnvironment);

  /**
   * 处理当前字体是否斜体
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据是否斜体，返回 {@code null}则按默认
   */
  Boolean handleFontItalic(GridEnvironment gridEnvironment);

  /**
   * 处理当前字体粗细
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体粗细，返回 {@code null}则按默认
   */
  Boolean handleFontBold(GridEnvironment gridEnvironment);

  /**
   * 处理当前是否加删除线
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据是否加删除线，返回 {@code null}则按默认
   */
  Boolean handleFontStrikeout(GridEnvironment gridEnvironment);

  /**
   * 处理后的数据的位置
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体类型偏移
   */
  FontStyle handleFontTypeOffset(GridEnvironment gridEnvironment);

  /**
   * 处理后的数据的字符集
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据的字符集
   */
  FontStyle handleFontCharSet(GridEnvironment gridEnvironment);

  /**
   * 处理后的是否包裹文本
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的是否包裹文本，返回 {@code null}则按默认
   */
  Boolean handleWrapText(GridEnvironment gridEnvironment);

  /**
   * 处理后的间隔
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的间隔，返回 {@code null}则按默认
   */
  Integer handleIndention(GridEnvironment gridEnvironment);

  /**
   * 处理后的旋转角度
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的旋转角度，返回 {@code null}则按默认
   */
  Integer handleRotation(GridEnvironment gridEnvironment);

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  Integer handleBottomBorderColor(GridEnvironment gridEnvironment);

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  Integer handleTopBorderColor(GridEnvironment gridEnvironment);

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  Integer handleRightBorderColor(GridEnvironment gridEnvironment);

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  Integer handleLeftBorderColor(GridEnvironment gridEnvironment);

  /**
   * 处理后的下划线
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的下划线，返回 {@code null}则按默认
   */
  TableDecoratorStyle handleUnderline(GridEnvironment gridEnvironment);

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  TableDecoratorStyle handleBorderBottom(GridEnvironment gridEnvironment);

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  TableDecoratorStyle handleBorderTop(GridEnvironment gridEnvironment);

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  TableDecoratorStyle handleBorderRight(GridEnvironment gridEnvironment);

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  TableDecoratorStyle handleBorderLeft(GridEnvironment gridEnvironment);

  /**
   * 处理后的垂直对齐方式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的垂直对齐方式，返回 {@code null}则按默认
   */
  TableDecoratorStyle handleVerticalAlignment(GridEnvironment gridEnvironment);

  /**
   * 处理后的水平对齐方式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的水平对齐方式，返回 {@code null}则按默认
   */
  TableDecoratorStyle handleHorizontalAlignment(GridEnvironment gridEnvironment);
}
