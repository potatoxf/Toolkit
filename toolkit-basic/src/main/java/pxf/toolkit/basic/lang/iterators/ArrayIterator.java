package pxf.toolkit.basic.lang.iterators;

import java.util.Iterator;

/**
 * 数组迭代器
 *
 * @author potatoxf
 * @date 2021/3/14
 */
public class ArrayIterator<T> implements Iterator<T> {

  /** 数组 */
  private final T[] array;
  /** 指针 */
  private int i;

  @SafeVarargs
  public ArrayIterator(T... array) {
    this.array = array;
    this.i = array == null ? Integer.MAX_VALUE : 0;
  }

  @Override
  public boolean hasNext() {
    return i < array.length;
  }

  @Override
  public T next() {
    return array[i++];
  }
}
