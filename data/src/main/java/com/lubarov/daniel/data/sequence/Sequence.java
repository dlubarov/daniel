package com.lubarov.daniel.data.sequence;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.option.Option;

import java.util.Random;

/**
 * A {@link Collection} of elements with a consistent, defined order of enumeration.
 */
public interface Sequence<A> extends Collection<A> {
  @Override
  <B> Sequence<B> map(Function<? super A, ? extends B> transformation);

  @Override
  Sequence<A> filter(Function<? super A, Boolean> predicate);

  @Override
  ImmutableSequence<A> toImmutable();

  @Override
  java.util.List<A> toJCF();

  A get(int index);

  A getFront();

  A getBack();

  /**
   * Get all elements except the first.
   */
  Sequence<A> getTail();

  /**
   * Get all elements except the last.
   */
  Sequence<A> getInit();

  Sequence<A> reversed();

  Sequence<A> shuffled(Random random);

  Sequence<A> replaceAll(A value, A replacement);

  Option<Integer> getFirstIndex(A value);

  Option<Integer> getLastIndex(A value);

  String join(String glue);
}
