package pxf.extsweb.fileonlinepreview;

import java.io.File;
import java.io.IOException;
import jodd.io.FileUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pxf.toolkit.basic.util.Resolve;

@Service
public class SimTextFilePreviewImpl implements FilePreview {

  private final OtherFilePreviewImpl otherFilePreview;

  public SimTextFilePreviewImpl(OtherFilePreviewImpl otherFilePreview) {
    this.otherFilePreview = otherFilePreview;
  }

  @Override
  public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
    String fileName = fileAttribute.getName();
    ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, fileName);
    if (response.isFailure()) {
      return otherFilePreview.notSupportedFile(model, fileAttribute, response.getMsg());
    }
    try {
      File originFile = new File(response.getContent());
      String charset = Resolve.charsetSimpleChinese(originFile, "UTF-8");
      String fileData = FileUtil.readString(originFile, charset);
      model.addAttribute("textData", Base64.encodeBase64String(fileData.getBytes()));
    } catch (IOException e) {
      return otherFilePreview.notSupportedFile(model, fileAttribute, e.getLocalizedMessage());
    }
    return TXT_FILE_PREVIEW_PAGE;
  }
}
