package pxf.toolkit.basic.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import pxf.toolkit.basic.constants.FindableConstant;

/**
 * 常量
 *
 * @author potatoxf
 * @date 2021/3/9
 */
public final class Const {

  /** 无效空白符 */
  public static final String INVALID_BLANK = " \t\n\r\f";
  /** 特殊字符--> {@code \t} */
  public static final char SC_T = 't';
  /** 特殊字符--> {@code \n} */
  public static final char SC_N = 'n';
  /** 特殊字符--> {@code \r} */
  public static final char SC_R = 'r';
  /** 特殊字符--> {@code \f} */
  public static final char SC_F = 'f';
  /** 特殊字符--> {@code \b} */
  public static final char SC_B = 'b';
  /** false */
  public static final boolean FALSE = false;
  /** true */
  public static final boolean TRUE = true;
  /** 单引号 */
  public static final char C_QUOTE = '\'';
  /** 双引号 */
  public static final char C_QUOTES = '"';
  /** 单引号 */
  public static final String S_QUOTE = "'";
  /** 双引号 */
  public static final String S_QUOTES = "\"";
  /** 零 */
  public static final byte ZERO_BYTE = 0;
  /** 零 */
  public static final short ZERO_SHORT = 0;
  /** 零 */
  public static final int ZERO_INT = 0;
  /** 零 */
  public static final long ZERO_LONG = 0;
  /** 零 */
  public static final float ZERO_FLOAT = 0;
  /** 零 */
  public static final double ZERO_DOUBLE = 0;
  /** Ascii 数字编码下界 */
  public static final int ASCII_NUMBER_LO = 48;
  /** Ascii 数字编码上界 */
  public static final int ASCII_NUMBER_HI = 57;
  /** Ascii 大写字母编码下界 */
  public static final int ASCII_UPPER_LETTER_LO = 65;
  /** Ascii 大写字母编码上界 */
  public static final int ASCII_UPPER_LETTER_HI = 90;
  /** Ascii 小写字母编码下界 */
  public static final int ASCII_LOWER_LETTER_LO = 97;
  /** Ascii 小写字母编码上界 */
  public static final int ASCII_LOWER_LETTER_HI = 122;
  /** 无效数字，适合所有数字返回值判断 */
  public static final int EOF = -1;
  /** 无效字符串，适合所有数字返回值判断 */
  public static final String EMPTY_STRING = "";
  /** 无效字符串，适合所有数字返回值判断 */
  public static final String NULL_STRING = "null";

  public static final String DEFAULT_DELIMITER = ",";

  /** 小写HEX */
  public static final char[] LOWER_HEX =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  /** 大写HEX */
  public static final char[] UPPER_HEX =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

  public static final Map<Character, Integer> LOWER_HEX_NUMBER_MAPPING =
      Map.ofEntries(
          Map.entry('0', 0),
          Map.entry('1', 1),
          Map.entry('2', 2),
          Map.entry('3', 3),
          Map.entry('4', 4),
          Map.entry('5', 5),
          Map.entry('6', 6),
          Map.entry('7', 7),
          Map.entry('8', 8),
          Map.entry('9', 9),
          Map.entry('a', 10),
          Map.entry('b', 11),
          Map.entry('c', 12),
          Map.entry('d', 13),
          Map.entry('e', 14),
          Map.entry('f', 15));
  public static final Map<Character, Integer> UPPER_HEX_NUMBER_MAPPING =
      Map.ofEntries(
          Map.entry('0', 0),
          Map.entry('1', 1),
          Map.entry('2', 2),
          Map.entry('3', 3),
          Map.entry('4', 4),
          Map.entry('5', 5),
          Map.entry('6', 6),
          Map.entry('7', 7),
          Map.entry('8', 8),
          Map.entry('9', 9),
          Map.entry('A', 10),
          Map.entry('B', 11),
          Map.entry('C', 12),
          Map.entry('D', 13),
          Map.entry('E', 14),
          Map.entry('F', 15));

  private Const() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 解析常量
   *
   * @param type 常量类型
   * @param input 输入
   * @param <T> 枚举类型
   * @return 返回{@code T extends Findable<T>}
   */
  public static <T extends FindableConstant<T>> T parseFindableConstant(Class<T> type, int input) {
    return parseFindableConstant(type, input, null);
  }

  /**
   * 解析常量
   *
   * @param type 常量类型
   * @param input 输入
   * @param defaultValue 默认值
   * @param <T> 枚举类型
   * @return 返回{@code T extends Findable<T>}
   */
  public static <T extends FindableConstant<T>> T parseFindableConstant(
      Class<T> type, int input, T defaultValue) {
    return parseFindable(type, input, defaultValue);
  }

  /**
   * 解析常量
   *
   * @param type 常量类型
   * @param input 输入
   * @param <T> 枚举类型
   * @return 返回{@code T extends Findable<T>}
   */
  public static <T extends FindableConstant<T>> T parseFindableConstant(
      Class<T> type, String input) {
    return parseFindableConstant(type, input, null);
  }

  /**
   * 解析常量
   *
   * @param type 常量类型
   * @param input 输入
   * @param defaultValue 默认值
   * @param <T> 枚举类型
   * @return 返回{@code T extends Findable<T>}
   */
  public static <T extends FindableConstant<T>> T parseFindableConstant(
      Class<T> type, String input, T defaultValue) {
    return parseFindable(type, input, defaultValue);
  }

  /**
   * 解析常量
   *
   * @param type 常量类型
   * @param input 输入
   * @param <T> 枚举类型
   * @return 返回{@code T extends Findable<T>}
   */
  public static <T extends FindableConstant<T>> T parseFindable(Class<T> type, int input) {
    return parseFindable(type, input, null);
  }

  /**
   * 解析常量
   *
   * @param type 常量类型
   * @param input 输入
   * @param <T> 枚举类型
   * @return 返回{@code T extends Findable<T>}
   */
  public static <T extends FindableConstant<T>> T parseFindable(
      Class<T> type, int input, T defaultValue) {
    try {
      if (FindableConstant.class.isAssignableFrom(type)) {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
          int modifiers = field.getModifiers();
          if (Modifier.isPublic(modifiers)
              && Modifier.isStatic(modifiers)
              && Modifier.isFinal(modifiers)
              && field.getType().equals(type)) {

            field.setAccessible(true);
            T result = (T) field.get(null);
            if (result.identity() == input) {
              return result;
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return defaultValue;
  }

  /**
   * 解析常量
   *
   * @param type 常量类型
   * @param input 输入
   * @param <T> 枚举类型
   * @return 返回{@code T extends Findable<T>}
   */
  public static <T extends FindableConstant<T>> T parseFindable(Class<T> type, String input) {
    return parseFindable(type, input, null);
  }

  /**
   * 解析常量
   *
   * @param type 常量类型
   * @param input 输入
   * @param defaultValue 默认值
   * @param <T> 枚举类型
   * @return 返回{@code T extends Findable<T>}
   */
  public static <T extends FindableConstant<T>> T parseFindable(
      Class<T> type, String input, T defaultValue) {
    try {
      if (FindableConstant.class.isAssignableFrom(type)) {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
          int modifiers = field.getModifiers();
          if (Modifier.isPublic(modifiers)
              && Modifier.isStatic(modifiers)
              && Modifier.isFinal(modifiers)
              && field.getType().equals(type)) {
            field.setAccessible(true);
            T result = (T) field.get(null);
            if (result == null) {
              continue;
            }
            if (result.isCustomMatchString() && result.isMatchString(input)) {
              return result;
            }
            String name = result.identityName();
            if (name == null) {
              continue;
            }
            if (result.isIgnoreNameCase()) {
              if (name.equalsIgnoreCase(input)) {
                return result;
              }
            } else {
              if (name.equals(input)) {
                return result;
              }
            }
            String[] alias = result.alias();
            if (alias == null || alias.length == 0) {
              continue;
            }
            for (String as : alias) {
              if (as == null) {
                continue;
              }
              if (result.isIgnoreAliasCase()) {
                if (as.equalsIgnoreCase(input)) {
                  return result;
                }
              } else {
                if (as.equals(input)) {
                  return result;
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return defaultValue;
  }
}
