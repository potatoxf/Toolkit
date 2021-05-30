package pxf.toolkit.components.model;

import java.io.Serializable;

/**
 * 属性信息
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public interface AttributeMessageData extends Serializable {

  /**
   * 获取属性值
   *
   * @return {@code String}
   */
  String attribute();

  /**
   * 获取信息值
   *
   * @return {@code String}
   */
  String message();
}
