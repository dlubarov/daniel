package com.lubarov.daniel.data.set;

import com.lubarov.daniel.data.collection.AbstractCollection;
import com.lubarov.daniel.data.function.Function;

public abstract class AbstractSet<A> extends AbstractCollection<A> implements Set<A> {
  // Don't let implementations inherit AbstractCollection's contains, which calls getCount.
  // Set implementations should generally provide their own contains.
  @Override
  public abstract boolean contains(A value);

  @Override
  public int getCount(A value) {
    return contains(value) ? 1 : 0;
  }

  @Override
  public Set<A> filter(Function<? super A, Boolean> predicate) {
    MutableHashSet<A> result = MutableHashSet.create();
    for (A element : this)
      if (predicate.apply(element))
        result.tryAdd(element);
    return result;
  }

  @Override
  public ImmutableSet<A> toImmutable() {
    return ImmutableHashSet.copyOf(this);
  }
}

