package com.lubarov.daniel.data.table.sequential;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.dictionary.functions.GetKeyFunction;
import com.lubarov.daniel.data.dictionary.functions.GetValueFunction;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.ImmutableSequence;
import com.lubarov.daniel.data.set.ImmutableHashSet;
import com.lubarov.daniel.data.set.Set;
import com.lubarov.daniel.data.source.Source;

/**
 * An immutable table backed by a simple array of key-value pairs.
 */
public final class ImmutableArrayTable<K, V>
    extends AbstractImmutableSequentialTable<K, V> {
  private final ImmutableSequence<KeyValuePair<K, V>> keyValuePairs;

  private ImmutableArrayTable(ImmutableSequence<KeyValuePair<K, V>> keyValuePairs) {
    this.keyValuePairs = keyValuePairs;
  }

  public static <K, V> ImmutableArrayTable<K, V> create() {
    return new ImmutableArrayTable<>(ImmutableArray.<KeyValuePair<K, V>>create());
  }

  public static <K, V> ImmutableArrayTable<K, V> copyOf(
      Iterable<KeyValuePair<K, V>> iterable) {
    return new ImmutableArrayTable<>(ImmutableArray.copyOf(iterable));
  }

  @Override
  public Set<K> getKeys() {
    return ImmutableHashSet.copyOf(keyValuePairs.map(new GetKeyFunction<K, V>()));
  }

  @Override
  public boolean containsKey(K key) {
    return !getValues(key).isEmpty();
  }

  @Override
  public Collection<V> getValues(K key) {
    Option<? extends Collection<KeyValuePair<K, V>>> optValues = keyValuePairs
        .groupBy(new GetKeyFunction<K, V>()).tryGetValue(key);
    if (optValues.isEmpty())
      return ImmutableArray.create();
    return optValues.getOrThrow().map(new GetValueFunction<K, V>());
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
