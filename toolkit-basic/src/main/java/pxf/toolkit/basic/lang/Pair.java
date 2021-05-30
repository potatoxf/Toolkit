package pxf.toolkit.basic.lang;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import javax.annotation.Nonnull;
import pxf.toolkit.basic.lang.iterators.ArrayIterator;

/**
 * 键值对
 *
 * @author potatoxf
 * @date 2021/3/21
 */
public class Pair<K, V>
    implements Serializable, Cloneable, Comparable<Pair<K, V>>, Iterable<Object> {

  /** 键 */
  private K key;
  /** 值 */
  private V value;

  /**
   * 获取键
   *
   * @return {@code K}
   */
  public K getKey() {
    return key;
  }

  /**
   * 设置键
   *
   * @param key {@code K}
   */
  public void setKey(K key) {
    this.key = key;
  }

  /**
   * 获取值
   *
   * @return {@code V}
   */
  public V getValue() {
    return value;
  }

  /**
   * 设置值
   *
   * @param value {@code V}
   */
  public void setValue(V value) {
    this.value = value;
  }

  @Override
  public int compareTo(Pair<K, V> o) {
    return 0;
  }

  @Nonnull
  @Override
  public Iterator<Object> iterator() {
    return new ArrayIterator<>(key, value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return getKey().equals(pair.getKey());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getKey());
  }

  @Override
  public String toString() {
    return key + " = " + value;
  }
}
