package pxf.toolkit.extension.office.manager;
/**
 * @author potatoxf
 * @date 2021/4/20
 */
public class OfficeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public OfficeException(String message) {
    super(message);
  }

  public OfficeException(String message, Throwable cause) {
    super(message, cause);
  }
}
