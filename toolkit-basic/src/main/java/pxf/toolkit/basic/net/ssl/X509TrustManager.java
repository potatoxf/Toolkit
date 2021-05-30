package pxf.toolkit.basic.net.ssl;

import java.security.cert.X509Certificate;

/**
 * 默认 X509TrustManager
 *
 * @author potatoxf
 * @date 2020/12/19
 */
public class X509TrustManager implements javax.net.ssl.X509TrustManager {

  @Override
  public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s) {}

  @Override
  public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s) {}

  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
  }
}
