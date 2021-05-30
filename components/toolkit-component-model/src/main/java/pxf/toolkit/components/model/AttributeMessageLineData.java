package pxf.toolkit.components.model;

/**
 * 行内属性信息
 *
 * @author potatoxf
 * @date 2021/4/1
 */
public interface AttributeMessageLineData {

  /**
   * 前置预留
   *
   * @return {@code double}
   */
  double topMargin();

  /**
   * 后置预留
   *
   * @return {@code double}
   */
  double bottomMargin();

  /**
   * 间隔
   *
   * @return {@code double[]}
   * @see #lineContents()
   */
  double[] leftMargin();

  /**
   * 行内消息
   *
   * @return AttributeMessageData[]
   */
  AttributeMessageData[] lineContents();
}
