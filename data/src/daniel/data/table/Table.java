package daniel.data.table;

import daniel.data.collection.Collection;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.set.Set;

/**
 * A collection of key-value pairs. Unlike a Dictionary, a Table allows multiple entries with
 * the same key.
 */
public interface Table<K, V> extends Collection<KeyValuePair<K, V>> {
  public Set<K> getKeys();

  public boolean containsKey(K key);

  public Collection<V> getValues(K key);

  @Override
  public ImmutableTable<K, V> toImmutable();
}
