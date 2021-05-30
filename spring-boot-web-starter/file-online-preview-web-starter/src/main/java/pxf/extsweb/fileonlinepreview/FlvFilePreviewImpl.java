package pxf.extsweb.fileonlinepreview;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class FlvFilePreviewImpl implements FilePreview {

  private final MediaFilePreviewImpl mediaFilePreview;

  public FlvFilePreviewImpl(MediaFilePreviewImpl mediaFilePreview) {
    this.mediaFilePreview = mediaFilePreview;
  }

  @Override
  public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
    mediaFilePreview.filePreviewHandle(url, model, fileAttribute);
    return FLV_FILE_PREVIEW_PAGE;
  }
}
