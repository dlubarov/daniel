package com.lubarov.daniel.data.dictionary.ordered;

import com.lubarov.daniel.data.dictionary.Dictionary;
import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.set.ordered.OrderedSet;

/**
 * A {@link Dictionary} with a consistent, defined of enumeration.
 */
public interface OrderedDictionary<K, V> extends Dictionary<K, V>, OrderedSet<KeyValuePair<K, V>> {
  @Override
  public ImmutableOrderedDictionary<K, V> filter(Function<? super KeyValuePair<K, V>, Boolean> predicate);

  @Override
  public ImmutableOrderedDictionary<K, V> toImmutable();
}
