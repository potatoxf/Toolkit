package pxf.toolkit.extension.office.format;

import java.util.Set;

/**
 * 文档格式注册
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public interface DocumentFormatRegistry {

  DocumentFormat getFormatByExtension(String extension);

  DocumentFormat getFormatByMediaType(String mediaType);

  Set<DocumentFormat> getOutputFormats(DocumentFamily family);
}
