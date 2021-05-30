package pxf.toolkit.extension.office.manager;

import java.net.ConnectException;
import pxf.toolkit.basic.constants.impl.LocalConnectionProtocol;
import pxf.toolkit.extension.office.OfficeTask;

/**
 * {@link OfficeManager} implementation that connects to an external Office process.
 *
 * <p>The external Office process needs to be started manually, e.g. from the command line with
 *
 * <pre>
 * soffice -accept="socket,host=127.0.0.1,port=2002;urp;"
 * </pre>
 *
 * <p>Since this implementation does not manage the Office process, it does not support
 * auto-restarting the process if it exits unexpectedly.
 *
 * <p>It will however auto-reconnect to the external process if the latter is manually restarted.
 *
 * <p>This {@link OfficeManager} implementation basically provides the same behaviour as
 * JODConverter 2.x, including using <em>synchronized</em> blocks for serialising office operations.
 *
 * @author potatoxf
 * @date 2021/4/29
 */
public class ExternalOfficeManager implements OfficeManager {

  private final OfficeConnection connection;
  private final boolean connectOnStart;

  /**
   * @param unoUrl
   * @param connectOnStart should a connection be attempted on {@link #start()}? Default is
   *     <em>true</em>. If <em>false</em>, a connection will only be attempted the first time an
   *     {@link OfficeTask} is executed.
   */
  public ExternalOfficeManager(UnoUrl unoUrl, boolean connectOnStart) {
    connection = new OfficeConnection(unoUrl);
    this.connectOnStart = connectOnStart;
  }

  @Override
  public void start() throws OfficeException {
    if (connectOnStart) {
      synchronized (connection) {
        connect();
      }
    }
  }

  @Override
  public void stop() {
    synchronized (connection) {
      if (connection.isConnected()) {
        connection.disconnect();
      }
    }
  }

  @Override
  public void execute(OfficeTask task) throws OfficeException {
    synchronized (connection) {
      if (!connection.isConnected()) {
        connect();
      }
      task.execute(connection);
    }
  }

  private void connect() {
    try {
      connection.connect();
    } catch (ConnectException connectException) {
      throw new OfficeException("could not connect to external office process", connectException);
    }
  }

  @Override
  public boolean isRunning() {
    return connection.isConnected();
  }

  public static class Builder {

    private LocalConnectionProtocol connectionProtocol = LocalConnectionProtocol.SOCKET;
    private int portNumber = 2002;
    private String pipeName = "office";
    private boolean connectOnStart = true;

    public void setConnectionProtocol(LocalConnectionProtocol connectionProtocol) {
      this.connectionProtocol = connectionProtocol;
    }

    public void setPortNumber(int portNumber) {
      this.portNumber = portNumber;
    }

    public void setPipeName(String pipeName) {
      this.pipeName = pipeName;
    }

    public void setConnectOnStart(boolean connectOnStart) {
      this.connectOnStart = connectOnStart;
    }

    public OfficeManager buildOfficeManager() {
      UnoUrl unoUrl =
          connectionProtocol == LocalConnectionProtocol.SOCKET
              ? UnoUrl.socket(portNumber)
              : UnoUrl.pipe(pipeName);
      return new ExternalOfficeManager(unoUrl, connectOnStart);
    }
  }
}
