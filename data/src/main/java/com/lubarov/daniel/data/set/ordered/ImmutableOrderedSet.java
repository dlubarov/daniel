package com.lubarov.daniel.data.set.ordered;

import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.sequence.ordered.ImmutableOrderedSequence;
import com.lubarov.daniel.data.set.ImmutableSet;

/**
 * A {@link OrderedSet} whose elements (and their order) never change.
 */
public interface ImmutableOrderedSet<A>
    extends OrderedSet<A>, ImmutableSet<A>, ImmutableOrderedSequence<A> {
  @Override
  public OrderedSet<A> filter(Function<? super A, Boolean> predicate);

  @Override
  public ImmutableOrderedSet<A> toImmutable();
}
