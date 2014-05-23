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
  public Source<A> getEnumerator();

  public int getSize();

  public boolean isEmpty();

  public int getCount(A value);

  public boolean contains(A value);

  public <B> Collection<B> map(Function<? super A, ? extends B> transformation);

  public Collection<A> filter(Function<? super A, Boolean> predicate);

  public <K> Dictionary<K, ? extends Collection<A>> groupBy(Function<? super A, ? extends K> grouper);

  public Sequence<A> sorted(Ordering<? super A> ordering);

  public Option<A> tryGetOnlyElement();

  public ImmutableCollection<A> toImmutable();

  public java.util.Collection<A> toJCF();

  public Object[] toArray();

  @Override
  public boolean equals(Object o);

  @Override
  public int hashCode();

  @Override
  public String toString();
}