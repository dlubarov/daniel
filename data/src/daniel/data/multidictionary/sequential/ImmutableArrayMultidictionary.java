package daniel.data.multidictionary.sequential;

import daniel.data.collection.Collection;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.functions.GetKeyFunction;
import daniel.data.dictionary.functions.GetValueFunction;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.set.ImmutableHashSet;
import daniel.data.set.Set;
import daniel.data.source.Source;

/**
 * An immutable multidictionary backed by a simple array of key-value pairs.
 */
public final class ImmutableArrayMultidictionary<K, V>
    extends AbstractImmutableSequentialMultidictionary<K, V> {
  private final ImmutableSequence<KeyValuePair<K, V>> keyValuePairs;

  private ImmutableArrayMultidictionary(ImmutableSequence<KeyValuePair<K, V>> keyValuePairs) {
    this.keyValuePairs = keyValuePairs;
  }

  public static <K, V> ImmutableArrayMultidictionary<K, V> copyOf(
      Iterable<KeyValuePair<K, V>> iterable) {
    return new ImmutableArrayMultidictionary<K, V>(ImmutableArray.copyOf(iterable));
  }

  @Override
  public Set<K> getKeys() {
    return ImmutableHashSet.copyOf(keyValuePairs.map(new GetKeyFunction<K, V>()));
  }

  @Override
  public Collection<V> getValues(K key) {
    return keyValuePairs.groupBy(new GetKeyFunction<K, V>()).getValue(key)
        .map(new GetValueFunction<K, V>());
  }

  @Override
  public KeyValuePair<K, V> get(int index) {
    return keyValuePairs.get(index);
  }

  @Override
  public Source<KeyValuePair<K, V>> getEnumerator() {
    return keyValuePairs.getEnumerator();
  }

  @Override
  public int getSize() {
    return keyValuePairs.getSize();
  }
}
