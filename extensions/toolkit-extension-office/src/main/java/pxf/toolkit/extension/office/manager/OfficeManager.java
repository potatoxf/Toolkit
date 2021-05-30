package pxf.toolkit.extension.office.manager;

import pxf.toolkit.extension.office.OfficeTask;

/**
 * @author potatoxf
 * @date 2021/4/20
 */
public interface OfficeManager {

  void execute(OfficeTask task) throws OfficeException;

  void start() throws OfficeException;

  void stop() throws OfficeException;

  boolean isRunning();
}
