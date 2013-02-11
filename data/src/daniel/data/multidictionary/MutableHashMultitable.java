package daniel.data.multidictionary;

import daniel.data.collection.Collection;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashTable;
import daniel.data.multiset.MutableHashMultiset;
import daniel.data.set.Set;
import daniel.data.source.Source;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public final class MutableHashMultitable<K, V> extends AbstractMultidictionary<K, V> {
  private final MutableHashTable<K, MutableHashMultiset<V>> valueGroups;
  private int size = 0;

  private MutableHashMultitable() {
    valueGroups = MutableHashTable.create();
  }

  public static <K, V> MutableHashMultitable<K, V> create() {
    return new MutableHashMultitable<>();
  }

  public static <K, V> MutableHashMultitable<K, V> copyOf(Iterable<KeyValuePair<K, V>> keyValuePairs) {
    MutableHashMultitable<K, V> result = create();
    for (KeyValuePair<K, V> keyValuePair : keyValuePairs)
      result.put(keyValuePair.getKey(), keyValuePair.getValue());
    return result;
  }

  public void put(K key, V value) {
    if (!valueGroups.containsKey(key))
      valueGroups.put(key, MutableHashMultiset.<V>create());
    valueGroups.getValue(key).add(value);
    ++size;
  }

  public boolean tryRemove(K key, V value) {
    if (!valueGroups.containsKey(key))
      return false;
    if (valueGroups.getValue(key).tryRemove(value)) {
      --size;
      return true;
    }
    return false;
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
  public boolean containsKey(K key) {
    return valueGroups.containsKey(key);
  }

  @Override
  public Collection<V> getValues(K key) {
    return valueGroups.tryGetValue(key).getOrDefault(MutableHashMultiset.<V>create());
  }

  @Override
  public Source<KeyValuePair<K, V>> getEnumerator() {
    MutableStack<KeyValuePair<K, V>> stack = DynamicArray.create();
    for (KeyValuePair<K, MutableHashMultiset<V>> keyAndValues : valueGroups)
      for (V value : keyAndValues.getValue())
        stack.pushBack(new KeyValuePair<>(keyAndValues.getKey(), value));
    return stack.getEnumerator();
  }

  @Override
  public int getSize() {
    return size;
  }
}
