package com.lubarov.daniel.data.table;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.set.Set;

/**
 * A collection of key-value pairs. Unlike a {@link daniel.data.dictionary.Dictionary}, it allows
 * multiple entries with the same key.
 */
public interface Table<K, V> extends Collection<KeyValuePair<K, V>> {
  public Set<K> getKeys();

  public boolean containsKey(K key);

  public Collection<V> getValues(K key);

  @Override
  public ImmutableTable<K, V> toImmutable();
}
