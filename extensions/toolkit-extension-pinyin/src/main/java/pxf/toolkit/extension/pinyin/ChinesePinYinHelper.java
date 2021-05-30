package pxf.toolkit.extension.pinyin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import pxf.toolkit.basic.lang.AsciiTableMatcher;
import pxf.toolkit.basic.lang.ExpandStringBuilder;
import pxf.toolkit.basic.lang.StringCase;
import pxf.toolkit.basic.util.Assert;
/**
 * 中文拼音助手
 *
 * @author potatoxf
 * @date 2021/4/29
 */
public final class ChinesePinYinHelper {
  private ChinesePinYinHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 获取拼音，并格式化成默认
   *
   * @param c 字符串
   * @return 格式化后的拼音
   * @see #gainPinYinWithFormat(char, StringCase, PinyinVCharType, PinyinToneType)
   */
  @Nonnull
  public static PinYin gainPinYinWithFormat(char c) {
    return gainPinYinWithFormat(c, null, null, null);
  }

  /**
   * 获取拼音
   *
   * @param c 字符串
   * @param StringCase 字符串大小写，默认 {@code StringCase.LOWER}
   * @param vCharType v字符类型，默认 {@code pxf.toolkit.extension.pinyin.PinyinVCharType.WITH_V}
   * @param toneType 音调类型，默认 {@code pxf.toolkit.extension.pinyin.PinyinToneType.WITHOUT_TONE}
   * @return 格式化后的拼音
   */
  @Nonnull
  public static PinYin gainPinYinWithFormat(
      char c,
      @Nullable StringCase StringCase,
      @Nullable PinyinVCharType vCharType,
      @Nullable PinyinToneType toneType) {
    PinYin pinYin = gainPinYin(c);
    String[] arr = new String[pinYin.length()];
    for (int i = 0; i < pinYin.length(); i++) {
      String pinYinStr = pinYin.get(i);
      arr[i] = PinyinFormat.format(pinYinStr, StringCase, vCharType, toneType);
    }
    return new PinYin(arr);
  }

  /**
   * 获取拼音
   *
   * @param c 字符串
   * @return {@code pxf.toolkit.extension.pinyin.PinYin}
   */
  @Nonnull
  public static PinYin gainPinYin(char c) {
    return PinYinRepository.CHINESE_4E00_9FA5.gainPinYin(c);
  }

  /**
   * 获取第一个拼音
   *
   * @param c 字符串
   * @return {@code String}
   */
  @Nonnull
  public static String gainFirstPinYin(char c) {
    return gainPinYin(c).get(0);
  }

  /**
   * 获取第一个拼音，并格式化成默认格式
   *
   * @param c 字符串
   * @return 格式化后的拼音字符串
   * @see #gainPinYinWithFormat(char, StringCase, PinyinVCharType, PinyinToneType)
   */
  @Nonnull
  public static String gainFirstPinYinWithFormat(char c) {
    return gainFirstPinYinWithFormat(c, null, null, null);
  }

  /**
   * 获取第一个拼音，并格式化
   *
   * @param c 字符串
   * @param StringCase 字符串大小写，默认 {@code StringCase.LOWER}
   * @param vCharType v字符类型，默认 {@code pxf.toolkit.extension.pinyin.PinyinVCharType.WITH_V}
   * @param toneType 音调类型，默认 {@code pxf.toolkit.extension.pinyin.PinyinToneType.WITHOUT_TONE}
   * @return 格式化后的拼音字符串
   */
  @Nonnull
  public static String gainFirstPinYinWithFormat(
      char c,
      @Nullable StringCase StringCase,
      @Nullable PinyinVCharType vCharType,
      @Nullable PinyinToneType toneType) {
    PinYin pinYin = gainPinYin(c);
    return PinyinFormat.format(pinYin.get(0), StringCase, vCharType, toneType);
  }

  /**
   * 获取全拼以空格分割，
   *
   * <p>它会清除里面不可见字符，并重新以空格分割
   *
   * @param sentence 句子
   * @return 拼音句子
   */
  @Nonnull
  public static String gainFullPinYin(@Nonnull String sentence) {
    return gainFullPinYin(sentence, null, null, null);
  }

  /**
   * 获取全拼以空格分割，
   *
   * <p>它会清除里面不可见字符，并重新以空格分割
   *
   * @param sentence 句子
   * @param StringCase 字符串大写还是小写
   * @param vCharType v字符格式
   * @param toneType 音调类型
   * @return 句子的拼音
   */
  @Nonnull
  public static String gainFullPinYin(
      @Nonnull String sentence,
      @Nullable StringCase StringCase,
      @Nullable PinyinVCharType vCharType,
      @Nullable PinyinToneType toneType) {
    Assert.noEmpty(sentence, "]sentence");
    int len = sentence.length();
    ExpandStringBuilder sb = new ExpandStringBuilder(len * 5);
    int englishCount = 0;
    for (int i = 0; i < len; i++) {
      char c = sentence.charAt(i);
      System.out.println((int) c - 0x4e00);
      if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.LETTER)) {
        englishCount++;
        continue;
      }
      // 不可见字符
      if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.INVISIBLE_CHAR)) {
        if (englishCount != 0) {
          sb.appendSpaceWithoutEmpty().append(sentence.substring(i - englishCount, i));
          englishCount = 0;
        }
        continue;
      }
      // 英文标点符号
      if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.PUNCTUATION)) {
        if (englishCount != 0) {
          sb.appendSpaceWithoutEmpty().append(sentence.substring(i - englishCount, i)).append(c);
          englishCount = 0;
        }
        continue;
      }
      // 支持中文
      if (isSupportPinYin(c)) {
        String pinyin = gainFirstPinYinWithFormat(c, StringCase, vCharType, toneType);
        sb.appendSpaceWithoutEmpty().append(pinyin);
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * 是否支持拼音
   *
   * @param c 字符
   * @return 如果支持返回 {@code true}，否则 {@code false}
   */
  public static boolean isSupportPinYin(char c) {
    return PinYinRepository.CHINESE_4E00_9FA5.isSupportPinYin(c);
  }

  /**
   * 获取拼音的首字符
   *
   * <p>当遇到英文时，根据参数是否保留拼音
   *
   * @param sentence 句子
   * @return 句子的拼音每个词的首字符
   */
  @Nonnull
  public static String gainSimplePinYin(@Nonnull String sentence) {
    return gainSimplePinYin(sentence, true, false, null, null, null);
  }

  /**
   * 获取拼音的首字符
   *
   * <p>当遇到英文时，根据参数是否保留拼音
   *
   * @param sentence 句子
   * @param isRetainFullEnglish 是否保留全拼英文
   * @param isAddSpace 是否添加空格
   * @param stringCase 字符串大写还是小写
   * @param vCharType v字符格式
   * @param toneType 音调类型
   * @return 句子的拼音每个词的首字符
   */
  @Nonnull
  public static String gainSimplePinYin(
      @Nonnull String sentence,
      boolean isRetainFullEnglish,
      boolean isAddSpace,
      @Nullable StringCase stringCase,
      @Nullable PinyinVCharType vCharType,
      @Nullable PinyinToneType toneType) {
    Assert.noEmpty(sentence, "]sentence");
    int len = sentence.length();
    ExpandStringBuilder sb = new ExpandStringBuilder(len * 5);
    int englishCount = 0;
    for (int i = 0; i < len; i++) {
      char c = sentence.charAt(i);
      if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.LETTER)) {
        englishCount++;
        continue;
      }
      // 不可见字符
      if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.INVISIBLE_CHAR)) {
        if (englishCount != 0) {
          if (!isAddSpace) {
            sb.appendSpaceWithoutEmpty();
          }
          String english = sentence.substring(i - englishCount, i);
          if (stringCase != null) {
            english = stringCase.handleStringCase(english);
          }
          if (isRetainFullEnglish) {
            sb.append(english);
          } else {
            sb.append(english.charAt(0));
          }
          englishCount = 0;
        }
        continue;
      }
      // 英文标点符号
      if (AsciiTableMatcher.isMatcherExceptAsciiChar(c, AsciiTableMatcher.PUNCTUATION)) {
        if (englishCount != 0) {
          if (!isAddSpace) {
            sb.appendSpaceWithoutEmpty();
          }
          String english = sentence.substring(i - englishCount, i);
          if (stringCase != null) {
            english = stringCase.handleStringCase(english);
          }
          if (isRetainFullEnglish) {
            sb.append(english);
          } else {
            sb.append(english.charAt(0));
          }
          sb.append(c);
          englishCount = 0;
        }
        continue;
      }
      // 支持中文
      if (isSupportPinYin(c)) {
        String pinyin = gainFirstPinYinWithFormat(c, stringCase, vCharType, toneType);
        if (!isAddSpace) {
          sb.appendSpaceWithoutEmpty();
        }
        sb.append(pinyin.charAt(0));
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * 获取拼音的首字符
   *
   * <p>当遇到英文时，根据参数是否保留拼音
   *
   * @param sentence 句子
   * @param isRetainFullEnglish 是否保留全拼英文
   * @return 句子的拼音每个词的首字符
   */
  @Nonnull
  public static String gainSimplePinYin(
      @Nonnull String sentence, boolean isRetainFullEnglish, boolean isMerge) {
    return gainSimplePinYin(sentence, isRetainFullEnglish, isMerge, null, null, null);
  }
}
