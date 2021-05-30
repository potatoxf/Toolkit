package pxf.infrastructure.system.setting;

import javax.annotation.Nonnull;
import pxf.toolkit.basic.constants.FindableEnumConstant;
import pxf.toolkit.basic.lang.TimeRange;
import pxf.toolkit.basic.text.parser.parameter.BooleanTextParameterParser;
import pxf.toolkit.basic.text.parser.parameter.DateTextParameterParser;
import pxf.toolkit.basic.text.parser.parameter.DoubleTextParameterParser;
import pxf.toolkit.basic.text.parser.parameter.IntegerTextParameterParser;
import pxf.toolkit.basic.text.parser.parameter.LongTextParameterParser;
import pxf.toolkit.basic.text.parser.parameter.StringTextParameterParser;
import pxf.toolkit.basic.text.parser.parameter.TextParameterParser;
import pxf.toolkit.basic.text.parser.parameter.TimeRangeTextParameterParser;
import pxf.toolkit.basic.util.Const;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.TimeHelper;

/**
 * 配置类型
 *
 * @author potatoxf
 * @date 2021/3/14
 */
public enum SettingType implements FindableEnumConstant<SettingType>, TextParameterParser<Object> {
  /** 配置类型 */
  BOOLEAN(new BooleanTextParameterParser(',', Const.FALSE)),
  INTEGER(new IntegerTextParameterParser(',', Const.ZERO_INT)),
  LONG(new LongTextParameterParser(',', Const.ZERO_LONG)),
  DOUBLE(new DoubleTextParameterParser(',', Const.ZERO_DOUBLE)),
  STRING(new StringTextParameterParser(',', Empty.STRING)),
  DATE(
      new DateTextParameterParser(
          ',', TimeHelper::nowDate, TimeHelper.DATE_FORMAT, TimeHelper::nowDate)),
  DATETIME(
      new DateTextParameterParser(
          ',', TimeHelper::nowDateTime, TimeHelper.DATE_TIME_SAFE_FORMAT, TimeHelper::nowDateTime)),
  DATE_RANGE(
      new TimeRangeTextParameterParser(
          ',', TimeRange::new, TimeHelper.DATE_FORMAT, TimeHelper::nowDate, '#')),
  DATETIME_RANGE(
      new TimeRangeTextParameterParser(
          ',', TimeRange::new, TimeHelper.DATE_TIME_SAFE_FORMAT, TimeHelper::nowDateTime, '#'));

  private final TextParameterParser<?> textParameterParser;

  SettingType(TextParameterParser<?> textParameterParser) {
    this.textParameterParser = textParameterParser;
  }

  /**
   * 获取支持结果类型
   *
   * @return 返回 {@code Class<?>}
   */
  @Nonnull
  @Override
  public Class<?> supportResultType() {
    return textParameterParser.supportResultType();
  }

  /**
   * 是否是支持的结果类型
   *
   * @param value 测试值
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  @Override
  public boolean isSupportResultType(Object value) {
    return textParameterParser.isSupportResultType(value);
  }

  /**
   * 解析单个值
   *
   * @param input 输入字符串
   * @return 返回解析后的结果
   */
  @Override
  public Object parseValue(String input) {
    return textParameterParser.parseValue(input);
  }

  /**
   * 解析多个值
   *
   * @param input 输入字符串
   * @return 返回解析后的结果
   */
  @Override
  public Object[] parseValues(String input) {
    return textParameterParser.parseValues(input);
  }

  public TextParameterParser<?> getTextParameterParser() {
    return textParameterParser;
  }
}
