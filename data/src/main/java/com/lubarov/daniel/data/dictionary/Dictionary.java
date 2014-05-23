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
  public ImmutableDictionary<K, V> toImmutable();

  public Option<V> tryGetValue(K key);

  public boolean containsKey(K key);

  public V getValue(K key);

  public Set<K> getKeys();

  public Collection<V> getValues();

  @Override
  public Dictionary<K, V> filter(Function<? super KeyValuePair<K, V>, Boolean> predicate);

  public Dictionary<K, V> filterKeys(Function<? super K, Boolean> predicate);

  public Dictionary<K, V> filterValues(Function<? super V, Boolean> predicate);
}
