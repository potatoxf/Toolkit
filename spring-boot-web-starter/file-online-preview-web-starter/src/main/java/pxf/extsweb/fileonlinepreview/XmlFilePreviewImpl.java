package pxf.extsweb.fileonlinepreview;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class XmlFilePreviewImpl implements FilePreview {

  private final SimTextFilePreviewImpl simTextFilePreview;

  public XmlFilePreviewImpl(SimTextFilePreviewImpl simTextFilePreview) {
    this.simTextFilePreview = simTextFilePreview;
  }

  @Override
  public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
    simTextFilePreview.filePreviewHandle(url, model, fileAttribute);
    return XML_FILE_PREVIEW_PAGE;
  }
}
