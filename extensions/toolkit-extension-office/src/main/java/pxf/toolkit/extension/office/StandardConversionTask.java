package pxf.toolkit.extension.office;

import com.sun.star.lang.XComponent;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XRefreshable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import pxf.toolkit.extension.office.format.DocumentFamily;
import pxf.toolkit.extension.office.format.DocumentFormat;
import pxf.toolkit.extension.office.manager.OfficeException;

/**
 * @author potatoxf
 * @date 2021/4/20
 */
public class StandardConversionTask extends AbstractConversionTask {

  private final DocumentFormat outputFormat;

  private Map<String, ?> defaultLoadProperties;
  private DocumentFormat inputFormat;

  public StandardConversionTask(File inputFile, File outputFile, DocumentFormat outputFormat) {
    super(inputFile, outputFile);
    this.outputFormat = outputFormat;
  }

  public void setDefaultLoadProperties(Map<String, ?> defaultLoadProperties) {
    this.defaultLoadProperties = defaultLoadProperties;
  }

  public void setInputFormat(DocumentFormat inputFormat) {
    this.inputFormat = inputFormat;
  }

  @Override
  protected void modifyDocument(XComponent document) throws OfficeException {
    XRefreshable refreshable = UnoRuntime.queryInterface(XRefreshable.class, document);
    if (refreshable != null) {
      refreshable.refresh();
    }
  }

  @Override
  protected Map<String, ?> getLoadProperties(File inputFile) {
    Map<String, Object> loadProperties = new HashMap<String, Object>();
    if (defaultLoadProperties != null) {
      loadProperties.putAll(defaultLoadProperties);
    }
    if (inputFormat != null && inputFormat.getLoadProperties() != null) {
      loadProperties.putAll(inputFormat.getLoadProperties());
    }
    return loadProperties;
  }

  @Override
  protected Map<String, ?> getStoreProperties(File outputFile, XComponent document) {
    DocumentFamily family = OfficeEnvironment.supportDocumentFamily(document);
    return outputFormat.getStoreProperties(family);
  }
}
