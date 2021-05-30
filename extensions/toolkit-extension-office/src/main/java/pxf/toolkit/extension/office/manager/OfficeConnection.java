package pxf.toolkit.extension.office.manager;

import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.NoConnectException;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XEventListener;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author potatoxf
 * @date 2021/4/20
 */
class OfficeConnection implements OfficeContext, XEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(OfficeConnection.class);
  private static final AtomicInteger BRIDGE_INDEX = new AtomicInteger();
  private final UnoUrl unoUrl;
  private final List<OfficeConnectionEventListener> connectionEventListeners = new ArrayList<>();
  private XComponent bridgeComponent;
  private XMultiComponentFactory serviceManager;
  private XComponentContext componentContext;
  private volatile boolean connected = false;

  public OfficeConnection(UnoUrl unoUrl) {
    this.unoUrl = unoUrl;
  }

  public void addConnectionEventListener(OfficeConnectionEventListener connectionEventListener) {
    connectionEventListeners.add(connectionEventListener);
  }

  public boolean isConnected() {
    return connected;
  }

  public synchronized void disconnect() {
    if (LOG.isWarnEnabled()) {
      LOG.warn("disconnecting: '{}'", unoUrl);
    }
    bridgeComponent.dispose();
  }

  public void connect() throws ConnectException {
    if (LOG.isInfoEnabled()) {
      LOG.info("connecting with connectString '{}'", unoUrl);
    }
    try {
      XComponentContext localContext = Bootstrap.createInitialComponentContext(null);
      XMultiComponentFactory localServiceManager = localContext.getServiceManager();
      XConnector connector =
          UnoRuntime.queryInterface(
              XConnector.class,
              localServiceManager.createInstanceWithContext(
                  "com.sun.star.connection.Connector", localContext));
      XConnection connection = connector.connect(unoUrl.getConnectString());
      XBridgeFactory bridgeFactory =
          UnoRuntime.queryInterface(
              XBridgeFactory.class,
              localServiceManager.createInstanceWithContext(
                  "com.sun.star.bridge.BridgeFactory", localContext));
      String bridgeName = "jodconverter_" + BRIDGE_INDEX.getAndIncrement();
      XBridge bridge = bridgeFactory.createBridge(bridgeName, "urp", connection, null);
      bridgeComponent = UnoRuntime.queryInterface(XComponent.class, bridge);
      bridgeComponent.addEventListener(this);
      serviceManager =
          UnoRuntime.queryInterface(
              XMultiComponentFactory.class, bridge.getInstance("StarOffice.ServiceManager"));
      XPropertySet properties = UnoRuntime.queryInterface(XPropertySet.class, serviceManager);
      componentContext =
          UnoRuntime.queryInterface(
              XComponentContext.class, properties.getPropertyValue("DefaultContext"));
      connected = true;
      if (LOG.isInfoEnabled()) {
        LOG.info("connected: '{}'", unoUrl);
      }
      OfficeConnectionEvent connectionEvent = new OfficeConnectionEvent(this);
      for (OfficeConnectionEventListener listener : connectionEventListeners) {
        listener.connected(connectionEvent);
      }
    } catch (NoConnectException connectException) {
      throw new ConnectException(
          String.format("connection failed: '%s'; %s", unoUrl, connectException.getMessage()));
    } catch (Exception exception) {
      throw new OfficeException("connection failed: " + unoUrl, exception);
    }
  }

  @Override
  public Object getService(String serviceName) {
    try {
      return serviceManager.createInstanceWithContext(serviceName, componentContext);
    } catch (Exception exception) {
      throw new OfficeException(
          String.format("failed to obtain service '%s'", serviceName), exception);
    }
  }

  @Override
  public void disposing(EventObject event) {
    if (connected) {
      connected = false;
      if (LOG.isInfoEnabled()) {
        LOG.info(String.format("disconnected: '%s'", unoUrl));
      }
      OfficeConnectionEvent connectionEvent = new OfficeConnectionEvent(this);
      for (OfficeConnectionEventListener listener : connectionEventListeners) {
        listener.disconnected(connectionEvent);
      }
    }
    // else we tried to connect to a server that doesn't speak URP
  }
}
