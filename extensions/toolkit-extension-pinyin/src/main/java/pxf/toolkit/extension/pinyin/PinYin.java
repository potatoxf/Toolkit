package pxf.toolkit.extension.pinyin;

import javax.annotation.Nonnull;
import pxf.toolkit.basic.lang.collection.ArrayObject;
/**
 * 拼音
 *
 * @author potatoxf
 * @date 2021/4/29
 */
public class PinYin extends ArrayObject<String> {
  private static final long serialVersionUID = 2163839972630926530L;

  PinYin(final String... array) {
    super(array);
  }

  PinYin(final PinYin pinYin) {
    super(pinYin);
  }

  PinYin(final PinYin pinYin, @Nonnull final String... array) {
    super(pinYin, array);
  }

  PinYin(final ArrayObject<String> arrayObject, @Nonnull final String... array) {
    super(arrayObject, array);
  }
  /**
   * 是否为多音字
   *
   * @return 如果 {@code true}则是多音字，否则false
   */
  public boolean isPoly() {
    return length() > 1;
  }
  /**
   * 是否为单音字
   *
   * @return 如果 {@code true}则是单音字，否则false
   */
  public boolean isMono() {
    return length() == 1;
  }
  /**
   * 去重
   *
   * @return {@code pxf.toolkit.extension.pinyin.PinYin}
   */
  PinYin distinct() {
    return new PinYin(super.distinct(String[]::new));
  }
}
