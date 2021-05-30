package pxf.toolkit.extension.office.manager;

import java.util.EventListener;

/**
 * @author potatoxf
 * @date 2021/4/20
 */
interface OfficeConnectionEventListener extends EventListener {

  void connected(OfficeConnectionEvent event);

  void disconnected(OfficeConnectionEvent event);
}
