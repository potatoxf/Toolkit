package pxf.toolkit.basic.lang.iterators;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import pxf.toolkit.basic.util.Assert;

/**
 * 综合迭代器
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public class CompositeIterator<E> implements Iterator<E> {

  private final Set<Iterator<E>> iterators = new LinkedHashSet<>();
  private boolean inUse = false;

  public void add(Iterator<E> iterator) {
    Assert.beTrue(
        !this.inUse,
        "You can no longer add iterators to a composite iterator that's already in use");
    if (this.iterators.contains(iterator)) {
      throw new IllegalArgumentException("You cannot add the same iterator twice");
    }
    this.iterators.add(iterator);
  }

  @Override
  public boolean hasNext() {
    this.inUse = true;
    for (Iterator<E> iterator : this.iterators) {
      if (iterator.hasNext()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public E next() {
    this.inUse = true;
    for (Iterator<E> iterator : this.iterators) {
      if (iterator.hasNext()) {
        return iterator.next();
      }
    }
    throw new NoSuchElementException("All iterators exhausted");
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("CompositeIterator does not support remove()");
  }
}
