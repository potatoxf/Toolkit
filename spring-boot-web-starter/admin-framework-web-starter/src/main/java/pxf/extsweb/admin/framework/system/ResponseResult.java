package pxf.extsweb.admin.framework.system;

/**
 * @author potatoxf
 * @date 2021/4/17
 */
public class ResponseResult {

  private final String code;
  private final String msg;
  private final Object data;

  protected ResponseResult(String code, String msg, Object data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public static ResponseResult of(String code, String msg) {
    return new ResponseResult(code, msg, null);
  }

  public static ResponseResult of(String code, String msg, Object data) {
    return new ResponseResult(code, msg, data);
  }

  public String getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

  public Object getData() {
    return data;
  }
}
