package com.lubarov.daniel.data.collection;

import com.lubarov.daniel.data.dictionary.Dictionary;
import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.order.Ordering;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.source.Source;

/**
 * A finite group of elements.
 *
 * @param <A> the type of the elements in the group
 */
public interface Collection<A> extends Iterable<A> {
  Source<A> getEnumerator();

  int getSize();

  boolean isEmpty();

  int getCount(A value);

  boolean contains(A value);

  <B> Collection<B> map(Function<? super A, ? extends B> transformation);

  Collection<A> filter(Function<? super A, Boolean> predicate);

  <K> Dictionary<K, ? extends Collection<A>> groupBy(Function<? super A, ? extends K> grouper);

  Sequence<A> sorted(Ordering<? super A> ordering);

  /** If the collection has exactly one element, return it, otherwise return None. */
  Option<A> tryGetOnlyElement();

  ImmutableCollection<A> toImmutable();

  java.util.Collection<A> toJCF();

  Object[] toArray();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
