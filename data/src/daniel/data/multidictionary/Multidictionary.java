package daniel.data.multidictionary;

import daniel.data.collection.Collection;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.set.Set;

public interface Multidictionary<K, V> extends Collection<KeyValuePair<K, V>> {
  public Set<K> getKeys();

  public Collection<V> getValues(K key);

  @Override
  public ImmutableMultidictionary<K, V> toImmutable();
}
