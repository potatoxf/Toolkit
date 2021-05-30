package pxf.extsweb.fileonlinepreview;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class OtherFilePreviewImpl implements FilePreview {

  @Override
  public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
    return this.notSupportedFile(model, fileAttribute, "系统还不支持该格式文件的在线预览");
  }

  /**
   * 通用的预览失败，导向到不支持的文件响应页面
   *
   * @return 页面
   */
  public String notSupportedFile(Model model, FileAttribute fileAttribute, String errMsg) {
    return this.notSupportedFile(model, fileAttribute.getSuffix(), errMsg);
  }

  /**
   * 通用的预览失败，导向到不支持的文件响应页面
   *
   * @return 页面
   */
  public String notSupportedFile(Model model, String errMsg) {
    return this.notSupportedFile(model, "未知", errMsg);
  }

  /**
   * 通用的预览失败，导向到不支持的文件响应页面
   *
   * @return 页面
   */
  public String notSupportedFile(Model model, String fileType, String errMsg) {
    model.addAttribute("fileType", fileType);
    model.addAttribute("msg", errMsg);
    return NOT_SUPPORTED_FILE_PAGE;
  }
}
