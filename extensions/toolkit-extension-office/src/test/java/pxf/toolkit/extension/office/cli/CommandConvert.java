package pxf.toolkit.extension.office.cli;

import java.io.File;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import pxf.toolkit.basic.util.Extract;
import pxf.toolkit.extension.office.OfficeDocumentConverter;
import pxf.toolkit.extension.office.format.DefaultDocumentFormatRegistry;
import pxf.toolkit.extension.office.format.DocumentFormatRegistry;
import pxf.toolkit.extension.office.format.JsonDocumentFormatRegistry;
import pxf.toolkit.extension.office.manager.OfficeManager;
import pxf.toolkit.extension.office.manager.OfficeManagerConfiguration;

/**
 * Convert Tester.
 *
 * @author <Authors name>
 *     <pre>3月 21, 2021</pre>
 *
 * @author potatoxf
 * @date 2021/5/2
 */
public class CommandConvert {

  public static final int STATUS_OK = 0;
  public static final int STATUS_MISSING_INPUT_FILE = 1;
  public static final int STATUS_INVALID_ARGUMENTS = 255;

  private static final Option OPTION_OUTPUT_FORMAT =
      new Option("o", "output-format", true, "output format (e.g. pdf)");
  private static final Option OPTION_PORT =
      new Option("p", "port", true, "office socket port (optional; defaults to 2002)");
  private static final Option OPTION_REGISTRY =
      new Option("r", "registry", true, "document formats registry configuration file (optional)");
  private static final Option OPTION_TIMEOUT =
      new Option(
          "t", "timeout", true, "maximum conversion time in seconds (optional; defaults to 120)");
  private static final Option OPTION_USER_PROFILE =
      new Option(
          "u",
          "user-profile",
          true,
          "use settings from the given user installation dir (optional)");
  private static final Options OPTIONS = initOptions();

  private static final int DEFAULT_OFFICE_PORT = 2002;

  private static Options initOptions() {
    Options options = new Options();
    options.addOption(OPTION_OUTPUT_FORMAT);
    options.addOption(OPTION_PORT);
    options.addOption(OPTION_REGISTRY);
    options.addOption(OPTION_TIMEOUT);
    options.addOption(OPTION_USER_PROFILE);
    return options;
  }

  public static void main(String[] arguments) throws ParseException, IOException, JSONException {
    CommandLineParser commandLineParser = new PosixParser();
    CommandLine commandLine = commandLineParser.parse(OPTIONS, arguments);

    String outputFormat = null;
    if (commandLine.hasOption(OPTION_OUTPUT_FORMAT.getOpt())) {
      outputFormat = commandLine.getOptionValue(OPTION_OUTPUT_FORMAT.getOpt());
    }

    int port = DEFAULT_OFFICE_PORT;
    if (commandLine.hasOption(OPTION_PORT.getOpt())) {
      port = Integer.parseInt(commandLine.getOptionValue(OPTION_PORT.getOpt()));
    }

    String[] fileNames = commandLine.getArgs();
    if ((outputFormat == null && fileNames.length != 2) || fileNames.length < 1) {
      String syntax =
          "java -jar jodconverter-core.jar [options] input-file output-file\n"
              + "or [options] -o output-format input-file [input-file...]";
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp(syntax, OPTIONS);
      System.exit(STATUS_INVALID_ARGUMENTS);
    }

    DocumentFormatRegistry registry;
    if (commandLine.hasOption(OPTION_REGISTRY.getOpt())) {
      File registryFile = new File(commandLine.getOptionValue(OPTION_REGISTRY.getOpt()));
      registry = new JsonDocumentFormatRegistry(FileUtils.readFileToString(registryFile));
    } else {
      registry = new DefaultDocumentFormatRegistry();
    }

    OfficeManagerConfiguration configuration = new OfficeManagerConfiguration();
    configuration.setPortNumbers(port);
    if (commandLine.hasOption(OPTION_TIMEOUT.getOpt())) {
      int timeout = Integer.parseInt(commandLine.getOptionValue(OPTION_TIMEOUT.getOpt()));
      configuration.setTaskExecutionTimeout(timeout * 1000);
    }
    if (commandLine.hasOption(OPTION_USER_PROFILE.getOpt())) {
      String templateProfileDir = commandLine.getOptionValue(OPTION_USER_PROFILE.getOpt());
      configuration.setTemplateProfileDir(templateProfileDir);
    }

    OfficeManager officeManager = configuration.buildOfficeManager();
    officeManager.start();
    OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager, registry);
    try {
      if (outputFormat == null) {
        File inputFile = new File(fileNames[0]);
        File outputFile = new File(fileNames[1]);
        converter.convert(inputFile, outputFile);
      } else {
        for (int i = 0; i < fileNames.length; i++) {
          File inputFile = new File(fileNames[i]);
          String outputName = Extract.fileBaseNameFormPath(fileNames[i]) + "." + outputFormat;
          File outputFile = new File(FilenameUtils.getFullPath(fileNames[i]) + outputName);
          converter.convert(inputFile, outputFile);
        }
      }
    } finally {
      officeManager.stop();
    }
  }
}
