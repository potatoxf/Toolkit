package pxf.toolkit.extension.office.format;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 简单文档格式注册器
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public class SimpleDocumentFormatRegistry implements DocumentFormatRegistry {

  private final List<DocumentFormat> documentFormats = new ArrayList<>();

  public void addFormat(DocumentFormat documentFormat) {
    documentFormats.add(documentFormat);
  }

  @Override
  public DocumentFormat getFormatByExtension(String extension) {
    if (extension == null) {
      return null;
    }
    String lowerExtension = extension.toLowerCase();
    for (DocumentFormat format : documentFormats) {
      if (lowerExtension.equals(format.getExtension())) {
        return format;
      }
    }
    return null;
  }

  @Override
  public DocumentFormat getFormatByMediaType(String mediaType) {
    if (mediaType == null) {
      return null;
    }
    for (DocumentFormat format : documentFormats) {
      if (mediaType.equals(format.getMediaType())) {
        return format;
      }
    }
    return null;
  }

  @Override
  public Set<DocumentFormat> getOutputFormats(DocumentFamily family) {
    Set<DocumentFormat> formats = new HashSet<>();
    for (DocumentFormat format : documentFormats) {
      if (format.getStoreProperties(family) != null) {
        formats.add(format);
      }
    }
    return formats;
  }
}
