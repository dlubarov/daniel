package com.lubarov.daniel.data.order;

import java.util.Comparator;

/**
 * An ordering of objects, similar to {@link Comparator}.
 */
public interface Ordering<A> {
  Relation compare(A a, A b);

  Ordering<A> reverse();

  Comparator<A> toComparator();
}
