package pxf.toolkit.components.model;

/**
 * 行内属性信息
 *
 * @author potatoxf
 * @date 2021/4/3
 */
public class DefaultAttributeMessageLineData implements AttributeMessageLineData {

  private double topMargin;
  private double bottomMargin;
  private double[] leftMargin;
  private AttributeMessageData[] lineContents;

  /**
   * 前置预留
   *
   * @return {@code double}
   */
  @Override
  public double topMargin() {
    return topMargin;
  }

  /**
   * 后置预留
   *
   * @return {@code double}
   */
  @Override
  public double bottomMargin() {
    return bottomMargin;
  }

  /**
   * 行内前置预留
   *
   * @return {@code double[]}
   * @see #lineContents()
   */
  @Override
  public double[] leftMargin() {
    return leftMargin;
  }

  /**
   * 行内消息
   *
   * @return AttributeMessageData[]
   */
  @Override
  public AttributeMessageData[] lineContents() {
    return lineContents;
  }

  public double getTopMargin() {
    return topMargin;
  }

  public void setTopMargin(double topMargin) {
    this.topMargin = topMargin;
  }

  public double getBottomMargin() {
    return bottomMargin;
  }

  public void setBottomMargin(double bottomMargin) {
    this.bottomMargin = bottomMargin;
  }

  public double[] getLeftMargin() {
    return leftMargin;
  }

  public void setLeftMargin(double... leftMargin) {
    this.leftMargin = leftMargin;
  }

  public AttributeMessageData[] getLineContents() {
    return lineContents;
  }

  public void setLineContents(AttributeMessageData... lineContents) {
    this.lineContents = lineContents;
  }
}
