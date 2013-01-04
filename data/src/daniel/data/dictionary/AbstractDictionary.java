package daniel.data.dictionary;

import daniel.data.collection.AbstractCollection;
import daniel.data.collection.Collection;
import daniel.data.dictionary.functions.GetKeyFunction;
import daniel.data.dictionary.functions.GetValueFunction;
import daniel.data.function.Function;
import daniel.data.set.ImmutableHashSet;
import daniel.data.set.Set;

public abstract class AbstractDictionary<K, V>
    extends AbstractCollection<KeyValuePair<K, V>>
    implements Dictionary<K, V> {
  @Override
  public ImmutableDictionary<K, V> toImmutable() {
    return ImmutableHashTable.copyOf(this);
  }

  @Override
  public boolean containsKey(K key) {
    return tryGetValue(key).isDefined();
  }

  @Override
  public V getValue(K key) {
    return tryGetValue(key).getOrThrow();
  }

  @Override
  public Set<K> getKeys() {
    return ImmutableHashSet.copyOf(this.map(new GetKeyFunction<K, V>()));
  }

  @Override
  public Collection<V> getValues() {
    return ImmutableHashSet.copyOf(this.map(new GetValueFunction<K, V>()));
  }

  @Override
  public Dictionary<K, V> filter(Function<? super KeyValuePair<K, V>, Boolean> predicate) {
    MutableHashTable<K, V> filteredDictionary = MutableHashTable.create();
    for (KeyValuePair<K, V> keyValuePair : this)
      filteredDictionary.put(keyValuePair.getKey(), keyValuePair.getValue());
    return filteredDictionary;
  }

  @Override
  public Dictionary<K, V> filterKeys(final Function<? super K, Boolean> predicate) {
    return this.filter(new Function<KeyValuePair<K, V>, Boolean>() {
      @Override public Boolean apply(KeyValuePair<K, V> keyValuePair) {
        return predicate.apply(keyValuePair.getKey());
      }
    });
  }

  @Override
  public Dictionary<K, V> filterValues(final Function<? super V, Boolean> predicate) {
    return this.filter(new Function<KeyValuePair<K, V>, Boolean>() {
      @Override public Boolean apply(KeyValuePair<K, V> keyValuePair) {
        return predicate.apply(keyValuePair.getValue());
      }
    });
  }
}
