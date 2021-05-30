package pxf.toolkit.components.model;

import java.util.Objects;

/**
 * 默认属性信息
 *
 * @author potatoxf
 * @date 2021/3/31
 */
public class DefaultAttributeMessageData implements AttributeMessageData {

  private final String headerName;
  private final String information;

  public DefaultAttributeMessageData(String headerName) {
    this(headerName, null);
  }

  public DefaultAttributeMessageData(String headerName, String information) {
    this.headerName = headerName;
    this.information = information;
  }

  @Override
  public String attribute() {
    return headerName;
  }

  @Override
  public String message() {
    return information;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultAttributeMessageData that = (DefaultAttributeMessageData) o;
    return headerName.equals(that.headerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(headerName);
  }

  @Override
  public String toString() {
    return headerName + " : " + information;
  }
}
