package pxf.extsweb.fileonlinepreview;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MediaFilePreviewImpl implements FilePreview {

  private final FileHandlerService fileHandlerService;
  private final OtherFilePreviewImpl otherFilePreview;

  public MediaFilePreviewImpl(
      FileHandlerService fileHandlerService, OtherFilePreviewImpl otherFilePreview) {
    this.fileHandlerService = fileHandlerService;
    this.otherFilePreview = otherFilePreview;
  }

  @Override
  public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
    // 不是http开头，浏览器不能直接访问，需下载到本地
    if (url != null && !url.toLowerCase().startsWith("http")) {
      ReturnResponse<String> response =
          DownloadUtils.downLoad(fileAttribute, fileAttribute.getName());
      if (response.isFailure()) {
        return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
      } else {
        model.addAttribute(
            "mediaUrl",
            BaseUrlFilter.getBaseUrl() + fileHandlerService.getRelativePath(response.getContent()));
      }
    } else {
      model.addAttribute("mediaUrl", url);
    }
    model.addAttribute("mediaUrl", url);
    return MEDIA_FILE_PREVIEW_PAGE;
  }
}
