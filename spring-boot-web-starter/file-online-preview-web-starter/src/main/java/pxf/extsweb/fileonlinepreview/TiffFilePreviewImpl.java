package pxf.extsweb.fileonlinepreview;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/** tiff 图片文件处理 */
@Service
public class TiffFilePreviewImpl implements FilePreview {

  private final PictureFilePreviewImpl pictureFilePreview;

  public TiffFilePreviewImpl(PictureFilePreviewImpl pictureFilePreview) {
    this.pictureFilePreview = pictureFilePreview;
  }

  @Override
  public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
    pictureFilePreview.filePreviewHandle(url, model, fileAttribute);
    return TIFF_FILE_PREVIEW_PAGE;
  }
}
