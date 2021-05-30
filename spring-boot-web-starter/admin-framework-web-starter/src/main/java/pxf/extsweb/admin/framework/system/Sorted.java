package pxf.extsweb.admin.framework.system;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
public interface Sorted<ENTITY extends Sorted<ENTITY>> extends Comparable<ENTITY> {

  /**
   * 排序的值
   *
   * @return 返回排序的值
   */
  int sortedValue();

  /**
   * 比较两数对象的大小
   *
   * @param other 其它对象
   * @return 返回排序大小数值 {@link Comparable}
   */
  @Override
  default int compareTo(ENTITY other) {
    return sortedValue() - other.sortedValue();
  }
}
