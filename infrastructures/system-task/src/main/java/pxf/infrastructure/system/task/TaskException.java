package pxf.infrastructure.system.task;

import pxf.toolkit.basic.ToolkitException;

/**
 * @author potatoxf
 * @date 2021/5/6
 */
public class TaskException extends ToolkitException {

  public TaskException(Throwable e) {
    super(e);
  }

  public TaskException(String message, Object... args) {
    super(message, args);
  }

  public TaskException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }
}
