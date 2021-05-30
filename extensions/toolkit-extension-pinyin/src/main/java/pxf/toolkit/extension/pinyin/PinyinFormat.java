package pxf.toolkit.extension.pinyin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import pxf.toolkit.basic.exception.AbnormalException;
import pxf.toolkit.basic.lang.StringCase;
import pxf.toolkit.basic.util.Assert;
/**
 * 拼音格式化
 *
 * @author potatoxf
 * @date 2021/4/29
 */
class PinyinFormat {
  private static final String A = "a";
  private static final String E = "e";
  private static final String OU = "ou";
  private static final String ALL_UNMARKED_VOWEL = "aeiouv";
  private static final String ALL_MARKED_VOWEL = "āáăàaēéĕèeīíĭìiōóŏòoūúŭùuǖǘǚǜü";
  /**
   * 格式化拼音
   *
   * @param pinyinStr 拼音字符粗
   * @param stringCase 字符串大小写，默认 {@code StringCase.LOWER}
   * @param vCharType v字符类型，默认 {@code pxf.toolkit.extension.pinyin.PinyinVCharType.WITH_V}
   * @param toneType 音调类型，默认 {@code pxf.toolkit.extension.pinyin.PinyinToneType.WITHOUT_TONE}
   * @return 格式化后的拼音字符串
   */
  public static String format(
      @Nonnull String pinyinStr,
      @Nullable StringCase stringCase,
      @Nullable PinyinVCharType vCharType,
      @Nullable PinyinToneType toneType) {
    Assert.noNull(pinyinStr, "]pin yin cn.cilisi.series.infrastructure.string");
    if (vCharType == null) {
      vCharType = PinyinVCharType.WITH_V;
    }
    if (toneType == null) {
      toneType = PinyinToneType.WITHOUT_TONE;
    }
    if (toneType == PinyinToneType.WITHOUT_TONE) {
      pinyinStr = pinyinStr.replaceAll("[1-5]", "");
    } else if (PinyinToneType.WITH_TONE_MARK == toneType) {
      if (vCharType != PinyinVCharType.WITH_U_UNICODE) {
        throw new IllegalArgumentException(
            "Please use [pxf.toolkit.extension.pinyin.PinyinVCharType.WITH_U_UNICODE],because tone marks cannot be added to v or u:");
      }
      pinyinStr = pinyinStr.replaceAll("u:", "v");
      pinyinStr = formatChineseToneMark(pinyinStr);
    }
    if (vCharType == PinyinVCharType.WITH_V) {
      pinyinStr = pinyinStr.replaceAll("u:", "v");
    } else if (PinyinVCharType.WITH_U_UNICODE == vCharType) {
      pinyinStr = pinyinStr.replaceAll("u:", "ü");
    }
    if (stringCase != null) {
      pinyinStr = stringCase.handleStringCase(pinyinStr);
    }
    return pinyinStr;
  }
  /**
   * 使用Unicode将音调数字转换为音调标记
   *
   * <p>确定出现音调标记的元音的简单算法如下：
   *
   * <ol>
   *   <li>首先，寻找“a”或“e”。如果出现任何一个元音，它将带有音调标记，不可能同时包含“ a”和“ e”的拼音音节。
   *   <li>如果没有“a”或“e”，则查找“ou”。如果出现“ou”，则“ o”带有号。
   *   <li>如果以上情况均不成立，则音节中的最后一个元音将带有音调标记。
   * </ol>
   *
   * @param pinyin 带有音调编号的 {@code ascii}表示
   * @return 带音标的 {@code unicode}表示
   */
  private static String formatChineseToneMark(String pinyin) {
    pinyin = pinyin.toLowerCase();
    int pinyinLen = pinyin.length();
    if (pinyin.matches("[a-z]*[1-5]?")) {
      String unmarkedVowel = null;
      int indexOfUnmarkedVowel;
      if (pinyin.matches("[a-z]*[1-5]")) {
        if ((indexOfUnmarkedVowel = pinyin.indexOf(A)) != -1) {
          unmarkedVowel = A;
        } else if ((indexOfUnmarkedVowel = pinyin.indexOf(E)) != -1) {
          unmarkedVowel = E;
        } else if ((indexOfUnmarkedVowel = pinyin.indexOf(OU)) != -1) {
          unmarkedVowel = OU.substring(0, 1);
        } else {
          for (int i = pinyinLen - 1; i >= 0; i--) {
            String c = pinyin.substring(i, i + 1);
            if (ALL_UNMARKED_VOWEL.contains(c)) {
              indexOfUnmarkedVowel = i;
              unmarkedVowel = c;
              break;
            }
          }
        }
        if (indexOfUnmarkedVowel != -1) {
          int tuneNumber = Character.getNumericValue(pinyin.charAt(pinyinLen - 1));
          int columnIndex = tuneNumber - 1;
          int rowIndex = ALL_UNMARKED_VOWEL.indexOf(unmarkedVowel);
          int vowelLocation = rowIndex * 5 + columnIndex;
          char markedVowel = ALL_MARKED_VOWEL.charAt(vowelLocation);
          return pinyin.substring(0, indexOfUnmarkedVowel).replace('v', 'ü')
              + markedVowel
              + pinyin.substring(indexOfUnmarkedVowel + 1, pinyinLen - 1).replace('v', 'ü');
          // error happens in the procedure of locating vowel
        } else {
          return pinyin;
        }
        // input cn.cilisi.series.infrastructure.string has no any tune number
      } else {
        // only replace v with ü (umlat) character
        return pinyin.replaceAll("v", "ü");
      }
    }
    throw new AbnormalException("Error formatting Chinese pinyin");
  }
}
