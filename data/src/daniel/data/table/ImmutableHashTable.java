package daniel.data.table;

import daniel.data.collection.Collection;
import daniel.data.collection.ImmutableCollection;
import daniel.data.dictionary.ImmutableDictionary;
import daniel.data.dictionary.ImmutableHashDictionary;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashDictionary;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.SinglyLinkedList;
import daniel.data.set.Set;
import daniel.data.source.Source;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

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
    return new ImmutableHashTable<>(ImmutableHashDictionary.<K, ImmutableCollection<V>>create());
  }

  public static <K, V> ImmutableHashTable<K, V> copyOf(
      Iterable<KeyValuePair<K, V>> keyValuePairs) {
    MutableHashDictionary<K, SinglyLinkedList<V>> valueGroups = MutableHashDictionary.create();
    for (KeyValuePair<K, V> keyValuePair : keyValuePairs) {
      K key = keyValuePair.getKey();
      V value = keyValuePair.getValue();
      SinglyLinkedList<V> oldGroup = valueGroups.tryGetValue(key)
          .getOrDefault(SinglyLinkedList.<V>create());
      valueGroups.put(key, oldGroup.pushFront(value));
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
        : ImmutableArray.<V>create();
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
