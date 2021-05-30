package pxf.toolkit.extension.pinyin;

/**
 * v字符类型
 *
 * @author potatoxf
 * @date 2021/4/29
 */
public enum PinyinVCharType {
  /** 该选项指示“ü”的输出为“u:” */
  WITH_U_AND_COLON,
  /** 该选项表示Unicode格式的“ü”输出为“ü” */
  WITH_U_UNICODE,
  /** 该选项指示“ü”的输出为“v” */
  WITH_V
}
