package pxf.extsweb.fileonlinepreview;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MarkdownFilePreviewImpl implements FilePreview {

  private final SimTextFilePreviewImpl simTextFilePreview;

  public MarkdownFilePreviewImpl(SimTextFilePreviewImpl simTextFilePreview) {
    this.simTextFilePreview = simTextFilePreview;
  }

  @Override
  public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
    simTextFilePreview.filePreviewHandle(url, model, fileAttribute);
    return MARKDOWN_FILE_PREVIEW_PAGE;
  }
}
