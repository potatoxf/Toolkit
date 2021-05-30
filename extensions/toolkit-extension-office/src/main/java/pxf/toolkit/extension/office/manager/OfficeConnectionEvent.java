package pxf.toolkit.extension.office.manager;

import java.util.EventObject;

/**
 * @author potatoxf
 * @date 2021/4/20
 */
class OfficeConnectionEvent extends EventObject {

  private static final long serialVersionUID = 2060652797570876077L;

  public OfficeConnectionEvent(OfficeConnection source) {
    super(source);
  }
}
