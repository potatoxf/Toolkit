package pxf.extsweb.fileonlinepreview;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class PictureFilePreviewImpl implements FilePreview {

  private final FileHandlerService fileHandlerService;
  private final OtherFilePreviewImpl otherFilePreview;

  public PictureFilePreviewImpl(
      FileHandlerService fileHandlerService, OtherFilePreviewImpl otherFilePreview) {
    this.fileHandlerService = fileHandlerService;
    this.otherFilePreview = otherFilePreview;
  }

  @Override
  public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
    List<String> imgUrls = new ArrayList<>();
    imgUrls.add(url);
    String fileKey = fileAttribute.getFileKey();
    List<String> zipImgUrls = fileHandlerService.getImgCache(fileKey);
    if (!CollectionUtils.isEmpty(zipImgUrls)) {
      imgUrls.addAll(zipImgUrls);
    }
    // 不是http开头，浏览器不能直接访问，需下载到本地
    if (url != null && !url.toLowerCase().startsWith("http")) {
      ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, null);
      if (response.isFailure()) {
        return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
      } else {
        String file = fileHandlerService.getRelativePath(response.getContent());
        imgUrls.clear();
        imgUrls.add(file);
        model.addAttribute("imgUrls", imgUrls);
        model.addAttribute("currentUrl", file);
      }
    } else {
      model.addAttribute("imgUrls", imgUrls);
      model.addAttribute("currentUrl", url);
    }
    return PICTURE_FILE_PREVIEW_PAGE;
  }
}
