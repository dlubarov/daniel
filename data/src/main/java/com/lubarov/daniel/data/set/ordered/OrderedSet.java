package com.lubarov.daniel.data.set.ordered;

import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.sequence.ordered.OrderedSequence;
import com.lubarov.daniel.data.set.Set;

/**
 * A {@link Set} whose elements are arranged according to some {@link daniel.data.order.Ordering}.
 */
public interface OrderedSet<A> extends Set<A>, OrderedSequence<A> {
  @Override
  public OrderedSet<A> filter(Function<? super A, Boolean> predicate);

  @Override
  public ImmutableOrderedSet<A> toImmutable();
}
