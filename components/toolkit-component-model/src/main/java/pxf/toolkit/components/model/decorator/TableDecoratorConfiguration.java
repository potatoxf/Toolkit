package pxf.toolkit.components.model.decorator;

import java.io.Serializable;
import java.util.Objects;
import pxf.toolkit.components.model.table.GridEnvironment;

/**
 * 表装饰配置
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public class TableDecoratorConfiguration extends TableDecoratorAdapter
    implements Serializable, TableDecorator {

  private Double scale;
  private Number minRowHeight;
  private Number maxRowHeight;
  private Number minColWidth;
  private Number maxColWidth;
  private Integer width;
  private Integer height;
  private Integer backgroundColor;
  private Integer foregroundColor;
  private String fontName;
  private Integer fontHeight;
  private Integer fontColor;
  private Boolean fontItalic;
  private Boolean fontBold;
  private Boolean fontStrikeout;
  private FontStyle fontTypeOffset;
  private FontStyle fontCharset;
  private Boolean wrapText;
  private Integer indention;
  private Integer rotation;
  private Integer bottomBorderColor;
  private Integer topBorderColor;
  private Integer rightBorderColor;
  private Integer leftBorderColor;
  private TableDecoratorStyle underline;
  private TableDecoratorStyle borderBottom;
  private TableDecoratorStyle borderTop;
  private TableDecoratorStyle borderRight;
  private TableDecoratorStyle borderLeft;
  private TableDecoratorStyle verticalAlignment;
  private TableDecoratorStyle horizontalAlignment;

  /**
   * 构建 {@code TableDecoratorConfiguration}
   *
   * @param decorator 装饰器
   * @param gridEnvironment 表格环境
   * @return {@code TableDecoratorConfiguration}
   */
  public static TableDecoratorConfiguration of(
      TableDecorator decorator, GridEnvironment gridEnvironment) {
    if (decorator == null) {
      return null;
    }
    TableDecoratorConfiguration configuration = new TableDecoratorConfiguration();
    setDecoratorConfiguration(configuration, decorator, gridEnvironment);
    return configuration;
  }

  /**
   * 将装饰器设置到装饰器配置
   *
   * @param configuration 表格装饰器配置
   * @param decorator 装饰器
   * @param gridEnvironment 表格环境
   */
  public static void setDecoratorConfiguration(
      TableDecoratorConfiguration configuration,
      TableDecorator decorator,
      GridEnvironment gridEnvironment) {
    configuration.setScale(decorator.scale());
    configuration.setMinRowHeight(decorator.minRowHeight());
    configuration.setMaxRowHeight(decorator.maxRowHeight());
    configuration.setMinColWidth(decorator.minColWidth());
    configuration.setMaxColWidth(decorator.maxColWidth());

    configuration.setBackgroundColor(decorator.handleBackgroundColor(gridEnvironment));
    configuration.setForegroundColor(decorator.handleForegroundColor(gridEnvironment));

    configuration.setFontName(decorator.handleFontName(gridEnvironment));
    configuration.setFontHeight(decorator.handleFontHeight(gridEnvironment));
    configuration.setFontColor(decorator.handleFontColor(gridEnvironment));
    configuration.setFontItalic(decorator.handleFontItalic(gridEnvironment));
    configuration.setFontBold(decorator.handleFontBold(gridEnvironment));
    configuration.setFontStrikeout(decorator.handleFontStrikeout(gridEnvironment));
    configuration.setFontTypeOffset(decorator.handleFontTypeOffset(gridEnvironment));
    configuration.setFontCharset(decorator.handleFontCharSet(gridEnvironment));
    configuration.setWrapText(decorator.handleWrapText(gridEnvironment));
    configuration.setIndention(decorator.handleIndention(gridEnvironment));
    configuration.setRotation(decorator.handleRotation(gridEnvironment));

    configuration.setBottomBorderColor(decorator.handleBottomBorderColor(gridEnvironment));
    configuration.setTopBorderColor(decorator.handleTopBorderColor(gridEnvironment));
    configuration.setRightBorderColor(decorator.handleRightBorderColor(gridEnvironment));
    configuration.setLeftBorderColor(decorator.handleLeftBorderColor(gridEnvironment));

    configuration.setUnderline(decorator.handleUnderline(gridEnvironment));

    configuration.setBorderBottom(decorator.handleBorderBottom(gridEnvironment));
    configuration.setBorderTop(decorator.handleBorderTop(gridEnvironment));
    configuration.setBorderRight(decorator.handleBorderRight(gridEnvironment));
    configuration.setBorderLeft(decorator.handleBorderLeft(gridEnvironment));

    configuration.setVerticalAlignment(decorator.handleVerticalAlignment(gridEnvironment));
    configuration.setHorizontalAlignment(decorator.handleHorizontalAlignment(gridEnvironment));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TableDecoratorConfiguration that = (TableDecoratorConfiguration) o;
    return Objects.equals(getWidth(), that.getWidth())
        && Objects.equals(getHeight(), that.getHeight())
        && Objects.equals(getBackgroundColor(), that.getBackgroundColor())
        && Objects.equals(getForegroundColor(), that.getForegroundColor())
        && Objects.equals(getFontName(), that.getFontName())
        && Objects.equals(getFontHeight(), that.getFontHeight())
        && Objects.equals(getFontColor(), that.getFontColor())
        && Objects.equals(getFontItalic(), that.getFontItalic())
        && Objects.equals(getFontBold(), that.getFontBold())
        && Objects.equals(getFontStrikeout(), that.getFontStrikeout())
        && getFontTypeOffset() == that.getFontTypeOffset()
        && getFontCharset() == that.getFontCharset()
        && Objects.equals(getWrapText(), that.getWrapText())
        && Objects.equals(getIndention(), that.getIndention())
        && Objects.equals(getRotation(), that.getRotation())
        && Objects.equals(getBottomBorderColor(), that.getBottomBorderColor())
        && Objects.equals(getTopBorderColor(), that.getTopBorderColor())
        && Objects.equals(getRightBorderColor(), that.getRightBorderColor())
        && Objects.equals(getLeftBorderColor(), that.getLeftBorderColor())
        && getUnderline() == that.getUnderline()
        && getBorderBottom() == that.getBorderBottom()
        && getBorderTop() == that.getBorderTop()
        && getBorderRight() == that.getBorderRight()
        && getBorderLeft() == that.getBorderLeft()
        && getVerticalAlignment() == that.getVerticalAlignment()
        && getHorizontalAlignment() == that.getHorizontalAlignment();
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getWidth(),
        getHeight(),
        getBackgroundColor(),
        getForegroundColor(),
        getFontName(),
        getFontHeight(),
        getFontColor(),
        getFontItalic(),
        getFontBold(),
        getFontStrikeout(),
        getFontTypeOffset(),
        getFontCharset(),
        getWrapText(),
        getIndention(),
        getRotation(),
        getBottomBorderColor(),
        getTopBorderColor(),
        getRightBorderColor(),
        getLeftBorderColor(),
        getUnderline(),
        getBorderBottom(),
        getBorderTop(),
        getBorderRight(),
        getBorderLeft(),
        getVerticalAlignment(),
        getHorizontalAlignment());
  }

  /**
   * 放缩比例
   *
   * @return 如果为 {@code null}，默认为 {@code 1}
   */
  @Override
  public Double scale() {
    return getScale();
  }

  /**
   * 最小行高
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  @Override
  public Number minRowHeight() {
    return getMinRowHeight();
  }

  /**
   * 最大行高
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  @Override
  public Number maxRowHeight() {
    return getMaxRowHeight();
  }

  /**
   * 最小列宽
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  @Override
  public Number minColWidth() {
    return getMinColWidth();
  }

  /**
   * 最大列宽
   *
   * @return {@code Number}，默认返回 {@code null}
   */
  @Override
  public Number maxColWidth() {
    return getMaxColWidth();
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
    return getWidth();
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
    return getHeight();
  }

  /**
   * 处理当前背景颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的背景颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleBackgroundColor(GridEnvironment gridEnvironment) {
    return getBackgroundColor();
  }

  /**
   * 处理当前前景颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的前景颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleForegroundColor(GridEnvironment gridEnvironment) {
    return getForegroundColor();
  }

  /**
   * 处理当前字体样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据，返回 {@code null}则按默认
   */
  @Override
  public String handleFontName(GridEnvironment gridEnvironment) {
    return getFontName();
  }

  /**
   * 处理当前字体高度
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据，返回 {@code null}则按默认
   */
  @Override
  public Integer handleFontHeight(GridEnvironment gridEnvironment) {
    return getFontHeight();
  }

  /**
   * 处理当前字体颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleFontColor(GridEnvironment gridEnvironment) {
    return getFontColor();
  }

  /**
   * 处理当前字体是否斜体
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据是否斜体，返回 {@code null}则按默认
   */
  @Override
  public Boolean handleFontItalic(GridEnvironment gridEnvironment) {
    return getFontItalic();
  }

  /**
   * 处理当前字体粗细
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的字体粗细，返回 {@code null}则按默认
   */
  @Override
  public Boolean handleFontBold(GridEnvironment gridEnvironment) {
    return getFontBold();
  }

  /**
   * 处理当前是否加删除线
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据是否加删除线，返回 {@code null}则按默认
   */
  @Override
  public Boolean handleFontStrikeout(GridEnvironment gridEnvironment) {
    return getFontStrikeout();
  }

  /**
   * 处理后的数据的位置
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据的位置
   */
  @Override
  public FontStyle handleFontTypeOffset(GridEnvironment gridEnvironment) {
    return getFontTypeOffset();
  }

  /**
   * 处理后的数据的字符集
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的数据的字符集
   */
  @Override
  public FontStyle handleFontCharSet(GridEnvironment gridEnvironment) {
    return getFontCharset();
  }

  /**
   * 处理后的是否包裹文本
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的是否包裹文本，返回 {@code null}则按默认
   */
  @Override
  public Boolean handleWrapText(GridEnvironment gridEnvironment) {
    return getWrapText();
  }

  /**
   * 处理后的间隔
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的间隔，返回 {@code null}则按默认
   */
  @Override
  public Integer handleIndention(GridEnvironment gridEnvironment) {
    return getIndention();
  }

  /**
   * 处理后的旋转角度
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的旋转角度，返回 {@code null}则按默认
   */
  @Override
  public Integer handleRotation(GridEnvironment gridEnvironment) {
    return getRotation();
  }

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleBottomBorderColor(GridEnvironment gridEnvironment) {
    return getBottomBorderColor();
  }

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleTopBorderColor(GridEnvironment gridEnvironment) {
    return getTopBorderColor();
  }

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleRightBorderColor(GridEnvironment gridEnvironment) {
    return getRightBorderColor();
  }

  /**
   * 处理后的边框颜色
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框颜色，返回 {@code null}则按默认
   */
  @Override
  public Integer handleLeftBorderColor(GridEnvironment gridEnvironment) {
    return getLeftBorderColor();
  }

  /**
   * 处理后的下划线
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的下划线，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleUnderline(GridEnvironment gridEnvironment) {
    return getUnderline();
  }

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleBorderBottom(GridEnvironment gridEnvironment) {
    return getBorderBottom();
  }

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleBorderTop(GridEnvironment gridEnvironment) {
    return getBorderTop();
  }

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleBorderRight(GridEnvironment gridEnvironment) {
    return getBorderRight();
  }

  /**
   * 处理后的边框样式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的边框样式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleBorderLeft(GridEnvironment gridEnvironment) {
    return getBorderLeft();
  }

  /**
   * 处理后的垂直对齐方式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的垂直对齐方式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleVerticalAlignment(GridEnvironment gridEnvironment) {
    return getVerticalAlignment();
  }

  /**
   * 处理后的水平对齐方式
   *
   * @param gridEnvironment 单元格所在的环境
   * @return 返回处理后的水平对齐方式，返回 {@code null}则按默认
   */
  @Override
  public TableDecoratorStyle handleHorizontalAlignment(GridEnvironment gridEnvironment) {
    return getHorizontalAlignment();
  }

  // --------------------------------------------------------------------------- get set

  public Double getScale() {
    return scale;
  }

  public void setScale(Double scale) {
    this.scale = scale;
  }

  public Number getMinRowHeight() {
    return minRowHeight;
  }

  public void setMinRowHeight(Number minRowHeight) {
    this.minRowHeight = minRowHeight;
  }

  public Number getMaxRowHeight() {
    return maxRowHeight;
  }

  public void setMaxRowHeight(Number maxRowHeight) {
    this.maxRowHeight = maxRowHeight;
  }

  public Number getMinColWidth() {
    return minColWidth;
  }

  public void setMinColWidth(Number minColWidth) {
    this.minColWidth = minColWidth;
  }

  public Number getMaxColWidth() {
    return maxColWidth;
  }

  public void setMaxColWidth(Number maxColWidth) {
    this.maxColWidth = maxColWidth;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Integer getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Integer backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public Integer getForegroundColor() {
    return foregroundColor;
  }

  public void setForegroundColor(Integer foregroundColor) {
    this.foregroundColor = foregroundColor;
  }

  public String getFontName() {
    return fontName;
  }

  public void setFontName(String fontName) {
    this.fontName = fontName;
  }

  public Integer getFontHeight() {
    return fontHeight;
  }

  public void setFontHeight(Integer fontHeight) {
    this.fontHeight = fontHeight;
  }

  public Integer getFontColor() {
    return fontColor;
  }

  public void setFontColor(Integer fontColor) {
    this.fontColor = fontColor;
  }

  public Boolean getFontItalic() {
    return fontItalic;
  }

  public void setFontItalic(Boolean fontItalic) {
    this.fontItalic = fontItalic;
  }

  public Boolean getFontBold() {
    return fontBold;
  }

  public void setFontBold(Boolean fontBold) {
    this.fontBold = fontBold;
  }

  public Boolean getFontStrikeout() {
    return fontStrikeout;
  }

  public void setFontStrikeout(Boolean fontStrikeout) {
    this.fontStrikeout = fontStrikeout;
  }

  public FontStyle getFontTypeOffset() {
    return fontTypeOffset;
  }

  public void setFontTypeOffset(FontStyle fontTypeOffset) {
    this.fontTypeOffset = fontTypeOffset;
  }

  public FontStyle getFontCharset() {
    return fontCharset;
  }

  public void setFontCharset(FontStyle fontCharset) {
    this.fontCharset = fontCharset;
  }

  public Boolean getWrapText() {
    return wrapText;
  }

  public void setWrapText(Boolean wrapText) {
    this.wrapText = wrapText;
  }

  public Integer getIndention() {
    return indention;
  }

  public void setIndention(Integer indention) {
    this.indention = indention;
  }

  public Integer getRotation() {
    return rotation;
  }

  public void setRotation(Integer rotation) {
    this.rotation = rotation;
  }

  public Integer getBottomBorderColor() {
    return bottomBorderColor;
  }

  public void setBottomBorderColor(Integer bottomBorderColor) {
    this.bottomBorderColor = bottomBorderColor;
  }

  public Integer getTopBorderColor() {
    return topBorderColor;
  }

  public void setTopBorderColor(Integer topBorderColor) {
    this.topBorderColor = topBorderColor;
  }

  public Integer getRightBorderColor() {
    return rightBorderColor;
  }

  public void setRightBorderColor(Integer rightBorderColor) {
    this.rightBorderColor = rightBorderColor;
  }

  public Integer getLeftBorderColor() {
    return leftBorderColor;
  }

  public void setLeftBorderColor(Integer leftBorderColor) {
    this.leftBorderColor = leftBorderColor;
  }

  public TableDecoratorStyle getUnderline() {
    return underline;
  }

  public void setUnderline(TableDecoratorStyle underline) {
    this.underline = underline;
  }

  public TableDecoratorStyle getBorderBottom() {
    return borderBottom;
  }

  public void setBorderBottom(TableDecoratorStyle borderBottom) {
    this.borderBottom = borderBottom;
  }

  public TableDecoratorStyle getBorderTop() {
    return borderTop;
  }

  public void setBorderTop(TableDecoratorStyle borderTop) {
    this.borderTop = borderTop;
  }

  public TableDecoratorStyle getBorderRight() {
    return borderRight;
  }

  public void setBorderRight(TableDecoratorStyle borderRight) {
    this.borderRight = borderRight;
  }

  public TableDecoratorStyle getBorderLeft() {
    return borderLeft;
  }

  public void setBorderLeft(TableDecoratorStyle borderLeft) {
    this.borderLeft = borderLeft;
  }

  public TableDecoratorStyle getVerticalAlignment() {
    return verticalAlignment;
  }

  public void setVerticalAlignment(TableDecoratorStyle verticalAlignment) {
    this.verticalAlignment = verticalAlignment;
  }

  public TableDecoratorStyle getHorizontalAlignment() {
    return horizontalAlignment;
  }

  public void setHorizontalAlignment(TableDecoratorStyle horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }
}
