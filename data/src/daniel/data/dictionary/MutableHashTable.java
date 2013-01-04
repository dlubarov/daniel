package daniel.data.dictionary;

import daniel.data.dictionary.functions.MapEntryConverter;
import daniel.data.option.Option;
import daniel.data.source.IteratorSource;
import daniel.data.source.Source;
import java.util.HashMap;
import java.util.Map;

public final class MutableHashTable<K, V> extends AbstractDictionary<K, V> {
  private final HashMap<K, V> proxy;

  private MutableHashTable(HashMap<K, V> proxy) {
    this.proxy = proxy;
  }

  public static <K, V> MutableHashTable<K, V> create() {
    return new MutableHashTable<>(new HashMap<K, V>());
  }

  public static <K, V> MutableHashTable<K, V> copyOf(
      Iterable<KeyValuePair<? extends K, ? extends V>> keyValuePairs) {
    HashMap<K, V> proxy = new HashMap<>();
    for (KeyValuePair<? extends K, ? extends V> keyValuePair : keyValuePairs)
      proxy.put(keyValuePair.getKey(), keyValuePair.getValue());
    return new MutableHashTable<>(proxy);
  }

  public static <K, V> MutableHashTable<K, V> copyOf(Map<? extends K, ? extends V> map) {
    return new MutableHashTable<>(new HashMap<>(map));
  }

  @Override
  public Option<V> tryGetValue(K key) {
    return Option.fromNullable(proxy.get(key));
  }

  public void put(K key, V value) {
    proxy.put(key, value);
  }

  @Override
  public Source<KeyValuePair<K, V>> getEnumerator() {
    return new IteratorSource<>(proxy.entrySet().iterator()).map(new MapEntryConverter<K, V>());
  }

  @Override
  public int getSize() {
    return proxy.size();
  }
}
