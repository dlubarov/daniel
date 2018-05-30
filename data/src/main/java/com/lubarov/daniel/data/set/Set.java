package com.lubarov.daniel.data.set;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.function.Function;

/**
 * A {@link Collection} which contains no duplicate elements.
 */
public interface Set<A> extends Collection<A> {
  @Override
  Set<A> filter(Function<? super A, Boolean> predicate);

  ImmutableSet<A> toImmutable();

  // TODO: intersection, union, etc.
  //public Set<A> intersection(Set<?> that);
}
