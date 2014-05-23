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
  public <B> Sequence<B> map(Function<? super A, ? extends B> transformation);

  @Override
  public Sequence<A> filter(Function<? super A, Boolean> predicate);

  @Override
  public ImmutableSequence<A> toImmutable();

  @Override
  public java.util.List<A> toJCF();

  public A get(int index);

  public A getFront();

  public A getBack();

  /**
   * Get all elements except the first.
   */
  public Sequence<A> getTail();

  /**
   * Get all elements except the last.
   */
  public Sequence<A> getInit();

  public Sequence<A> reversed();

  public Sequence<A> shuffled(Random random);

  public Sequence<A> replaceAll(A value, A replacement);

  public Option<Integer> getFirstIndex(A value);

  public Option<Integer> getLastIndex(A value);

  public String join(String glue);
}
