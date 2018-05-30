package com.lubarov.daniel.data.sequence.ordered;

import com.lubarov.daniel.data.order.Ordering;
import com.lubarov.daniel.data.sequence.Sequence;

/**
 * A {@link Sequence} whose elements are always ordered according to some {@link Ordering}.
 */
public interface OrderedSequence<A> extends Sequence<A> {
  Ordering<A> getOrdering();
}
