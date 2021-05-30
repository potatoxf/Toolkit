package pxf.toolkit.extension.pinyin;

/**
 * 定义汉语拼音输出格式
 *
 * <p>中文有四个音调和一个“无声”音调。它们分别被称为： Píng（平），Shǎng（上）， Qù（去），Rù（入）和Qing（轻）。
 *
 * <p>通常，我们使用1、2、3、4和5来*表示它们。
 *
 * @author potatoxf
 * @date 2021/4/29
 */
public enum PinyinToneType {
  /** 该选项表示汉字拼音输出有音调编号 */
  WITH_TONE_NUMBER,
  /** 该选项表示输出的汉语拼音没有音调编号或音调标记 */
  WITHOUT_TONE,
  /** 该选项表示汉语拼音输出带有音调标记 */
  WITH_TONE_MARK
}
