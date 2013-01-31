package daniel.data.multidictionary;

import daniel.data.collection.Collection;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashTable;
import daniel.data.sequence.ImmutableArray;
import daniel.data.set.Set;
import daniel.data.source.Source;
import daniel.data.stack.DynamicArray;

public final class MutableHashMultitable<K, V> extends AbstractMultidictionary<K, V> {
  private final MutableHashTable<K, DynamicArray<V>> valueGroups;
  private int size = 0;

  private MutableHashMultitable() {
    valueGroups = MutableHashTable.create();
  }

  public static <K, V> MutableHashMultitable<K, V> create() {
    return new MutableHashMultitable<>();
  }

  public void put(K key, V value) {
    if (!valueGroups.containsKey(key))
      valueGroups.put(key, DynamicArray.<V>create());
    valueGroups.getValue(key).pushBack(value);
    ++size;
  }

  public boolean tryRemove(K key) {
    if (valueGroups.containsKey(key)) {
      int n = valueGroups.getValue(key).getSize();
      valueGroups.tryRemove(key);
      size -= n;
      return n > 0;
    }
    return false;
  }

  @Override
  public Set<K> getKeys() {
    return valueGroups.getKeys();
  }

  @Override
  public Collection<V> getValues(K key) {
    return valueGroups.tryGetValue(key).getOrDefault(DynamicArray.<V>create());
  }

  @Override
  public Source<KeyValuePair<K, V>> getEnumerator() {
    return ImmutableArray.copyOf(this).getEnumerator();
  }

  @Override
  public int getSize() {
    return size;
  }
}
