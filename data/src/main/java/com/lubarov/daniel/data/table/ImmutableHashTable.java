package com.lubarov.daniel.data.table;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.collection.ImmutableCollection;
import com.lubarov.daniel.data.dictionary.ImmutableDictionary;
import com.lubarov.daniel.data.dictionary.ImmutableHashDictionary;
import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.dictionary.MutableHashDictionary;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.SinglyLinkedList;
import com.lubarov.daniel.data.set.Set;
import com.lubarov.daniel.data.source.Source;
import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;

public final class ImmutableHashTable<K, V> extends AbstractImmutableTable<K, V> {
  private final ImmutableDictionary<K, ? extends ImmutableCollection<V>> valueGroups;
  private final int size;

  private ImmutableHashTable(ImmutableDictionary<K, ? extends ImmutableCollection<V>> valueGroups) {
    this.valueGroups = valueGroups;
    int totalSize = 0;
    for (Collection<V> valueGroup : valueGroups.getValues())
      totalSize += valueGroup.getSize();
    this.size = totalSize;
  }

  public static <K, V> ImmutableHashTable<K, V> create() {
    return new ImmutableHashTable<>(ImmutableHashDictionary.create());
  }

  public static <K, V> ImmutableHashTable<K, V> copyOf(
      Iterable<KeyValuePair<K, V>> keyValuePairs) {
    MutableHashDictionary<K, SinglyLinkedList<V>> valueGroups = MutableHashDictionary.create();
    for (KeyValuePair<K, V> keyValuePair : keyValuePairs) {
      K key = keyValuePair.getKey();
      V value = keyValuePair.getValue();
      SinglyLinkedList<V> oldGroup = valueGroups.tryGetValue(key)
          .getOrDefault(SinglyLinkedList.create());
      valueGroups.put(key, oldGroup.plusFront(value));
    }
    return new ImmutableHashTable<>(valueGroups.toImmutable());
  }

  @Override
  public Set<K> getKeys() {
    return valueGroups.getKeys();
  }

  @Override
  public boolean containsKey(K key) {
    return valueGroups.containsKey(key);
  }

  @Override
  public Collection<V> getValues(K key) {
    return valueGroups.containsKey(key)
        ? valueGroups.getValue(key)
        : ImmutableArray.create();
  }

  @Override
  public Source<KeyValuePair<K, V>> getEnumerator() {
    MutableStack<KeyValuePair<K, V>> stack = DynamicArray.create();
    for (KeyValuePair<K, ? extends ImmutableCollection<V>> keyAndValues : valueGroups)
      for (V value : keyAndValues.getValue())
        stack.pushBack(new KeyValuePair<>(keyAndValues.getKey(), value));
    return stack.getEnumerator();
  }

  @Override
  public int getSize() {
    return size;
  }
}
