package pxf.extsweb.fileonlinepreview;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pxf.toolkit.extension.office.OfficeEnvironment;

@Component
public class ConfigRefreshComponent {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigRefreshComponent.class);

  @PostConstruct
  void refresh() {
    Thread configRefreshThread = new Thread(new ConfigRefreshThread());
    configRefreshThread.start();
  }

  static class ConfigRefreshThread implements Runnable {

    @Override
    public void run() {
      try {
        String text;
        String media;
        boolean cacheEnabled;
        String[] textArray;
        String[] mediaArray;
        String officePreviewType;
        String officePreviewSwitchDisabled;
        String ftpUsername;
        String ftpPassword;
        String ftpControlEncoding;
        String configFilePath = OfficeEnvironment.getInstance().getOfficeCustomizedConfigPath();
        String baseUrl;
        String trustHost;
        String pdfDownloadDisable;
        while (true) {
          Properties properties = OfficeEnvironment.getInstance().loadConfig();
          cacheEnabled =
              Boolean.parseBoolean(
                  properties.getProperty("cache.enabled", ConfigConstants.DEFAULT_CACHE_ENABLED));
          text = properties.getProperty("simText", ConfigConstants.DEFAULT_TXT_TYPE);
          media = properties.getProperty("media", ConfigConstants.DEFAULT_MEDIA_TYPE);
          officePreviewType =
              properties.getProperty(
                  "office.preview.type", ConfigConstants.DEFAULT_OFFICE_PREVIEW_TYPE);
          officePreviewSwitchDisabled =
              properties.getProperty(
                  "office.preview.switch.disabled", ConfigConstants.DEFAULT_OFFICE_PREVIEW_TYPE);
          ftpUsername =
              properties.getProperty("ftp.username", ConfigConstants.DEFAULT_FTP_USERNAME);
          ftpPassword =
              properties.getProperty("ftp.password", ConfigConstants.DEFAULT_FTP_PASSWORD);
          ftpControlEncoding =
              properties.getProperty(
                  "ftp.control.encoding", ConfigConstants.DEFAULT_FTP_CONTROL_ENCODING);
          textArray = text.split(",");
          mediaArray = media.split(",");
          baseUrl = properties.getProperty("base.url", ConfigConstants.DEFAULT_BASE_URL);
          trustHost = properties.getProperty("trust.host", ConfigConstants.DEFAULT_TRUST_HOST);
          pdfDownloadDisable =
              properties.getProperty(
                  "pdf.download.disable", ConfigConstants.DEFAULT_PDF_DOWNLOAD_DISABLE);
          ConfigConstants.setCacheEnabledValueValue(cacheEnabled);
          ConfigConstants.setSimTextValue(textArray);
          ConfigConstants.setMediaValue(mediaArray);
          ConfigConstants.setOfficePreviewTypeValue(officePreviewType);
          ConfigConstants.setFtpUsernameValue(ftpUsername);
          ConfigConstants.setFtpPasswordValue(ftpPassword);
          ConfigConstants.setFtpControlEncodingValue(ftpControlEncoding);
          ConfigConstants.setBaseUrlValue(baseUrl);
          ConfigConstants.setTrustHostValue(trustHost);
          ConfigConstants.setOfficePreviewSwitchDisabledValue(officePreviewSwitchDisabled);
          ConfigConstants.setPdfDownloadDisableValue(pdfDownloadDisable);
          setWatermarkConfig(properties);
          TimeUnit.SECONDS.sleep(1);
        }
      } catch (InterruptedException e) {
        LOGGER.error("读取配置文件异常", e);
      }
    }

    private void setWatermarkConfig(Properties properties) {
      String watermarkTxt =
          properties.getProperty("watermark.txt", WatermarkConfigConstants.DEFAULT_WATERMARK_TXT);
      String watermarkXSpace =
          properties.getProperty(
              "watermark.x.space", WatermarkConfigConstants.DEFAULT_WATERMARK_X_SPACE);
      String watermarkYSpace =
          properties.getProperty(
              "watermark.y.space", WatermarkConfigConstants.DEFAULT_WATERMARK_Y_SPACE);
      String watermarkFont =
          properties.getProperty("watermark.font", WatermarkConfigConstants.DEFAULT_WATERMARK_FONT);
      String watermarkFontsize =
          properties.getProperty(
              "watermark.fontsize", WatermarkConfigConstants.DEFAULT_WATERMARK_FONTSIZE);
      String watermarkColor =
          properties.getProperty(
              "watermark.color", WatermarkConfigConstants.DEFAULT_WATERMARK_COLOR);
      String watermarkAlpha =
          properties.getProperty(
              "watermark.alpha", WatermarkConfigConstants.DEFAULT_WATERMARK_ALPHA);
      String watermarkWidth =
          properties.getProperty(
              "watermark.width", WatermarkConfigConstants.DEFAULT_WATERMARK_WIDTH);
      String watermarkHeight =
          properties.getProperty(
              "watermark.height", WatermarkConfigConstants.DEFAULT_WATERMARK_HEIGHT);
      String watermarkAngle =
          properties.getProperty(
              "watermark.angle", WatermarkConfigConstants.DEFAULT_WATERMARK_ANGLE);
      WatermarkConfigConstants.setWatermarkTxtValue(watermarkTxt);
      WatermarkConfigConstants.setWatermarkXSpaceValue(watermarkXSpace);
      WatermarkConfigConstants.setWatermarkYSpaceValue(watermarkYSpace);
      WatermarkConfigConstants.setWatermarkFontValue(watermarkFont);
      WatermarkConfigConstants.setWatermarkFontsizeValue(watermarkFontsize);
      WatermarkConfigConstants.setWatermarkColorValue(watermarkColor);
      WatermarkConfigConstants.setWatermarkAlphaValue(watermarkAlpha);
      WatermarkConfigConstants.setWatermarkWidthValue(watermarkWidth);
      WatermarkConfigConstants.setWatermarkHeightValue(watermarkHeight);
      WatermarkConfigConstants.setWatermarkAngleValue(watermarkAngle);
    }
  }
}
