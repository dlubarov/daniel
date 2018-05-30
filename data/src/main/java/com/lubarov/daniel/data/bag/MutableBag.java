package com.lubarov.daniel.data.bag;

import com.lubarov.daniel.data.collection.Collection;

/**
 * A {@link daniel.data.set.Set}-like structure which can contain duplicate elements.
 */
public interface MutableBag<A> extends Collection<A> {
  void add(A value);

  void add(A value, int count);

  boolean tryRemove(A value);

  boolean tryRemove(A value, int count);

  void setCount(A value, int count);
}
