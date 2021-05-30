package pxf.toolkit.extension.office;

import com.sun.star.document.UpdateDocMode;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import pxf.toolkit.basic.util.Extract;
import pxf.toolkit.extension.office.format.DefaultDocumentFormatRegistry;
import pxf.toolkit.extension.office.format.DocumentFormat;
import pxf.toolkit.extension.office.format.DocumentFormatRegistry;
import pxf.toolkit.extension.office.manager.OfficeException;
import pxf.toolkit.extension.office.manager.OfficeManager;

/**
 * @author potatoxf
 * @date 2021/4/20
 */
public class OfficeDocumentConverter {

  private final OfficeManager officeManager;
  private final DocumentFormatRegistry formatRegistry;

  private Map<String, ?> defaultLoadProperties = createDefaultLoadProperties();

  public OfficeDocumentConverter(OfficeManager officeManager) {
    this(officeManager, new DefaultDocumentFormatRegistry());
  }

  public OfficeDocumentConverter(
      OfficeManager officeManager, DocumentFormatRegistry formatRegistry) {
    this.officeManager = officeManager;
    this.formatRegistry = formatRegistry;
  }

  private Map<String, Object> createDefaultLoadProperties() {
    Map<String, Object> loadProperties = new HashMap<String, Object>();
    loadProperties.put("Hidden", true);
    loadProperties.put("ReadOnly", true);
    loadProperties.put("UpdateDocMode", UpdateDocMode.QUIET_UPDATE);
    return loadProperties;
  }

  public void setDefaultLoadProperties(Map<String, ?> defaultLoadProperties) {
    this.defaultLoadProperties = defaultLoadProperties;
  }

  public DocumentFormatRegistry getFormatRegistry() {
    return formatRegistry;
  }

  public void convert(File inputFile, File outputFile) throws OfficeException {
    String outputExtension = Extract.fileExtensionFormPath(outputFile.getName());
    DocumentFormat outputFormat = formatRegistry.getFormatByExtension(outputExtension);
    convert(inputFile, outputFile, outputFormat);
  }

  public void convert(File inputFile, File outputFile, DocumentFormat outputFormat)
      throws OfficeException {
    String inputExtension = Extract.fileExtensionFormPath(inputFile.getName());
    DocumentFormat inputFormat = formatRegistry.getFormatByExtension(inputExtension);
    StandardConversionTask conversionTask =
        new StandardConversionTask(inputFile, outputFile, outputFormat);
    conversionTask.setDefaultLoadProperties(defaultLoadProperties);
    conversionTask.setInputFormat(inputFormat);
    officeManager.execute(conversionTask);
  }
}
