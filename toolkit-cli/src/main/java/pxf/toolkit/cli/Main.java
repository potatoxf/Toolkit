package pxf.toolkit.cli;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import pxf.toolkit.basic.lang.iterators.LineIterable;

/**
 * @author potatoxf
 * @date 2021/5/7
 */
public class Main {

  private static final Options OPTIONS = new Options();
  private static final CommandLineParser COMMAND_LINE_PARSER = new DefaultParser();
  private static String HELP_STRING;

  static {
    // help
    OPTIONS.addOption("help", "usage help");
    // srcPath
    OPTIONS.addOption(
        Option.builder("s")
            .required()
            .hasArg(true)
            .longOpt("src")
            .type(String.class)
            .desc("the srcPath of local")
            .build());
    // destPath
    OPTIONS.addOption(
        Option.builder("d")
            .required()
            .hasArg(true)
            .longOpt("dest")
            .type(String.class)
            .desc("the dstPath of remote")
            .build());
  }
  /**
   * get string of help usage
   *
   * @return help string
   */
  private static String getHelpString() {
    if (HELP_STRING == null) {
      HelpFormatter helpFormatter = new HelpFormatter();

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
      helpFormatter.printHelp(
          printWriter,
          HelpFormatter.DEFAULT_WIDTH,
          "toolkit --help",
          null,
          OPTIONS,
          HelpFormatter.DEFAULT_LEFT_PAD,
          HelpFormatter.DEFAULT_DESC_PAD,
          null);
      printWriter.flush();
      HELP_STRING = new String(byteArrayOutputStream.toByteArray());
      printWriter.close();
    }
    return HELP_STRING;
  }

  public static void main(String[] args) throws Exception {
    CommandLine commandLine = COMMAND_LINE_PARSER.parse(OPTIONS, args);
    String src = commandLine.getOptionValue('s');
    String dest = commandLine.getOptionValue('d');
    deduplicationByLine(src, dest);
  }

  public static void deduplicationByLine(String src, String dest) throws IOException {
    FileWriter fileWriter = new FileWriter(dest);
    LineIterable lineIterable = new LineIterable(src);
    Set<String> set = new HashSet<>();
    lineIterable.forEach(set::add);
    IOUtils.writeLines(set, null, fileWriter);
    fileWriter.close();
  }
}
