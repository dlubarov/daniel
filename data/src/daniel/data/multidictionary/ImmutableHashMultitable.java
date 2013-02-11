package daniel.data.multidictionary;

import daniel.data.collection.Collection;
import daniel.data.collection.ImmutableCollection;
import daniel.data.dictionary.ImmutableDictionary;
import daniel.data.dictionary.ImmutableHashTable;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashTable;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.SinglyLinkedList;
import daniel.data.set.Set;
import daniel.data.source.Source;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public final class ImmutableHashMultitable<K, V> extends AbstractImmutableMultidictionary<K, V> {
  private final ImmutableDictionary<K, ? extends ImmutableCollection<V>> valueGroups;
  private final int size;

  private ImmutableHashMultitable(
      ImmutableDictionary<K, ? extends ImmutableCollection<V>> valueGroups) {
    this.valueGroups = valueGroups;
    int totalSize = 0;
    for (Collection<V> valueGroup : valueGroups.getValues())
      totalSize += valueGroup.getSize();
    this.size = totalSize;
  }

  public static <K, V> ImmutableHashMultitable<K, V> create() {
    return new ImmutableHashMultitable<>(ImmutableHashTable.<K, ImmutableCollection<V>>create());
  }

  public static <K, V> ImmutableHashMultitable<K, V> copyOf(
      Iterable<KeyValuePair<K, V>> keyValuePairs) {
    MutableHashTable<K, SinglyLinkedList<V>> valueGroups = MutableHashTable.create();
    for (KeyValuePair<K, V> keyValuePair : keyValuePairs) {
      K key = keyValuePair.getKey();
      V value = keyValuePair.getValue();
      SinglyLinkedList<V> oldGroup = valueGroups.tryGetValue(key)
          .getOrDefault(SinglyLinkedList.<V>create());
      valueGroups.put(key, oldGroup.pushFront(value));
    }
    return new ImmutableHashMultitable<>(valueGroups.toImmutable());
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
