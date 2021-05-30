package pxf.toolkit.extension.office.manager;
/**
 * @author potatoxf
 * @date 2021/4/29
 */
public class OfficeConnectionConfiguration extends OfficeManagerConfiguration {

  public static final long DEFAULT_RETRY_INTERVAL = 250L;
  private final UnoUrl unoUrl;
  private long retryInterval = DEFAULT_RETRY_INTERVAL;

  public OfficeConnectionConfiguration(UnoUrl unoUrl) {
    this.unoUrl = unoUrl;
  }

  public UnoUrl getUnoUrl() {
    return unoUrl;
  }

  public long getRetryInterval() {
    return retryInterval;
  }

  public void setRetryInterval(long retryInterval) {
    this.retryInterval = retryInterval;
  }
}
