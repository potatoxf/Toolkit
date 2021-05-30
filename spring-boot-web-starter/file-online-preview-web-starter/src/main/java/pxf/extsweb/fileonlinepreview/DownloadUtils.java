package pxf.extsweb.fileonlinepreview;

import io.mola.galimatias.GalimatiasParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.util.Extract;
import pxf.toolkit.basic.util.Is;

public class DownloadUtils {

  private static final Logger logger = LoggerFactory.getLogger(DownloadUtils.class);
  private static final String fileDir = ConfigConstants.getFileDir();
  private static final String URL_PARAM_FTP_USERNAME = "ftp.username";
  private static final String URL_PARAM_FTP_PASSWORD = "ftp.password";
  private static final String URL_PARAM_FTP_CONTROL_ENCODING = "ftp.control.encoding";

  /**
   * @param fileAttribute fileAttribute
   * @param fileName 文件名
   * @return 本地文件绝对路径
   */
  public static ReturnResponse<String> downLoad(FileAttribute fileAttribute, String fileName) {
    String urlStr = fileAttribute.getUrl();
    ReturnResponse<String> response = new ReturnResponse<>(0, "下载成功!!!", "");
    String realPath = DownloadUtils.getRelFilePath(fileName, fileAttribute);
    try {
      URL url = io.mola.galimatias.URL.parse(urlStr).toJavaURL();
      if (Is.httpUrl(url) || Is.fileUrl(url)) {
        File realFile = new File(realPath);
        FileUtils.copyURLToFile(url, realFile);
      } else if (Is.fileUrl(url)) {
        Map<String, String> map = Extract.parameterFromUrl(fileAttribute.getUrl());
        String ftpUsername = map.get(URL_PARAM_FTP_USERNAME);
        String ftpPassword = map.get(URL_PARAM_FTP_PASSWORD);
        String ftpControlEncoding = map.get(URL_PARAM_FTP_CONTROL_ENCODING);
        FtpUtils.download(
            fileAttribute.getUrl(), realPath, ftpUsername, ftpPassword, ftpControlEncoding);
      } else {
        response.setCode(1);
        response.setMsg("url不能识别url" + urlStr);
      }
      response.setContent(realPath);
      response.setMsg(fileName);
      return response;
    } catch (IOException | GalimatiasParseException e) {
      logger.error("文件下载失败，url：{}", urlStr, e);
      response.setCode(1);
      response.setContent(null);
      if (e instanceof FileNotFoundException) {
        response.setMsg("文件不存在!!!");
      } else {
        response.setMsg(e.getMessage());
      }
      return response;
    }
  }

  /**
   * 获取真实文件绝对路径
   *
   * @param fileName 文件名
   * @return 文件路径
   */
  private static String getRelFilePath(String fileName, FileAttribute fileAttribute) {
    String type = fileAttribute.getSuffix();
    if (null == fileName) {
      UUID uuid = UUID.randomUUID();
      fileName = uuid + "." + type;
    } else { // 文件后缀不一致时，以type为准(针对simText【将类txt文件转为txt】)
      fileName = fileName.replace(fileName.substring(fileName.lastIndexOf(".") + 1), type);
    }
    String realPath = fileDir + fileName;
    File dirFile = new File(fileDir);
    if (!dirFile.exists() && !dirFile.mkdirs()) {
      logger.error("创建目录【{}】失败,可能是权限不够，请检查", fileDir);
    }
    return realPath;
  }
}
