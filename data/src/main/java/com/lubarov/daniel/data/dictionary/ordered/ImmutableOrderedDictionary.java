package com.lubarov.daniel.data.dictionary.ordered;

import com.lubarov.daniel.data.dictionary.ImmutableDictionary;
import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.set.ordered.ImmutableOrderedSet;

/**
 * An {@link OrderedDictionary} whose elements never change.
 */
public interface ImmutableOrderedDictionary<K, V> extends
    OrderedDictionary<K, V>, ImmutableDictionary<K, V>, ImmutableOrderedSet<KeyValuePair<K, V>> {
  @Override
  public ImmutableOrderedDictionary<K, V> filter(Function<? super KeyValuePair<K, V>, Boolean> predicate);

  @Override
  public ImmutableOrderedDictionary<K, V> toImmutable();
}
