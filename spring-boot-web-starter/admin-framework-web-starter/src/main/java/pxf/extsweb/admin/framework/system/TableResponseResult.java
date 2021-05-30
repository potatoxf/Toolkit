package pxf.extsweb.admin.framework.system;

import java.util.Collection;

/**
 * @author potatoxf
 * @date 2021/4/17
 */
public class TableResponseResult extends ResponseResult {

  private final int count;

  public TableResponseResult(String code, String msg, Collection<?> data) {
    super(code, msg, data);
    this.count = data == null ? 0 : data.size();
  }

  public static TableResponseResult of(String code, String msg) {
    return new TableResponseResult(code, msg, null);
  }

  public static TableResponseResult of(String code, String msg, Collection<?> data) {
    return new TableResponseResult(code, msg, data);
  }

  public int getCount() {
    return count;
  }
}
