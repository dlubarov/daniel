package com.lubarov.daniel.data.dictionary;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.set.Set;

/**
 * A structure which maps keys to values. It cannot contain duplicate keys.
 */
public interface Dictionary<K, V> extends Set<KeyValuePair<K, V>> {
  @Override
  ImmutableDictionary<K, V> toImmutable();

  Option<V> tryGetValue(K key);

  boolean containsKey(K key);

  V getValue(K key);

  Set<K> getKeys();

  Collection<V> getValues();

  @Override
  Dictionary<K, V> filter(Function<? super KeyValuePair<K, V>, Boolean> predicate);

  Dictionary<K, V> filterKeys(Function<? super K, Boolean> predicate);

  Dictionary<K, V> filterValues(Function<? super V, Boolean> predicate);
}
