package pxf.toolkit.basic.text.parser.capitulation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.annotation.Nonnull;
import pxf.toolkit.basic.util.Is;

/**
 * 抽象规则金额转换
 *
 * @author potatoxf
 * @date 2021/5/22
 */
public abstract class AbstractCapitalizedTextParser implements CapitalizedTextParser {

  private static final String POSITIVE_PREFIX = "+";

  private static final String NEGATIVE_PREFIX = "-";

  private int decimalBit = 3;

  /**
   * 解析合格金额
   *
   * @param number 阿拉伯数字金额
   * @return 返回正规的金额数字
   */
  @Nonnull
  @Override
  public final String parse(@Nonnull String number) {
    if (!Is.numberForHuman(number)) {
      throw new IllegalArgumentException("Don't arabic numerals");
    }
    int lastI = number.lastIndexOf(".");
    String integer;
    String decimal;
    if (lastI == -1) {
      integer = number;
      decimal = null;
    } else {
      integer = number.substring(0, lastI);
      decimal = number.substring(lastI + 1);
    }
    if (integer.length() > configMaxIntegerLength()) {
      throw new IllegalArgumentException(
          "Integer bit don't allow greater than " + configMaxIntegerLength());
    }
    if (decimal != null) {
      BigDecimal bigDecimal = new BigDecimal("0." + decimal);
      bigDecimal = bigDecimal.setScale(this.decimalBit, RoundingMode.HALF_UP);
      decimal = bigDecimal.toPlainString().substring(2);
      decimal = parseDecimal(decimal);
    }
    boolean isNegative = false;
    if (integer.startsWith(POSITIVE_PREFIX) || integer.startsWith(NEGATIVE_PREFIX)) {
      if (integer.startsWith(NEGATIVE_PREFIX)) {
        isNegative = true;
      }
      integer = integer.substring(1);
    }
    integer = parseInteger(integer);
    return marge(isNegative, integer, decimal);
  }

  /**
   * 配置最大小数长度
   *
   * @return {@code int}
   */
  protected abstract int configMaxDecimalLength();

  /**
   * 配置最大整数长度
   *
   * @return {@code int}
   */
  protected abstract int configMaxIntegerLength();

  /**
   * 合并正规整数和小数部分
   *
   * @param isNegative 是否为负数
   * @param integer 整数部分
   * @param decimal 小数部分，没有返回空字符串
   * @return 合并成正规的数字
   */
  protected abstract String marge(boolean isNegative, String integer, String decimal);

  /**
   * 解析小数部分
   *
   * @param amount 小数
   * @return 返回正规小数
   */
  protected abstract String parseDecimal(String amount);

  /**
   * 解析整数部分
   *
   * @param amount 整数
   * @return 返回正规整数
   */
  protected abstract String parseInteger(String amount);

  public int getDecimalBit() {
    return this.decimalBit;
  }

  public void setDecimalBit(int decimalBit) {
    if (decimalBit > configMaxIntegerLength()) {
      throw new IllegalArgumentException(
          "Don't support decimal bit more than " + configMaxDecimalLength());
    }
    this.decimalBit = decimalBit;
  }
}
