package daniel.data.dictionary;

import daniel.data.dictionary.functions.MapEntryConverter;
import daniel.data.option.Option;
import daniel.data.source.IteratorSource;
import daniel.data.source.Source;
import java.util.HashMap;
import java.util.Map;

public final class ImmutableHashTable<K, V> extends AbstractImmutableDictionary<K, V> {
  private final HashMap<K, V> proxy;

  private ImmutableHashTable(HashMap<K, V> proxy) {
    this.proxy = proxy;
  }

  public static <K, V> ImmutableHashTable<K, V> create() {
    return new ImmutableHashTable<>(new HashMap<K, V>());
  }

  public static <K, V> ImmutableHashTable<K, V> copyOf(Iterable<KeyValuePair<K, V>> keyValuePairs) {
    HashMap<K, V> proxy = new HashMap<>();
    for (KeyValuePair<? extends K, ? extends V> keyValuePair : keyValuePairs)
      proxy.put(keyValuePair.getKey(), keyValuePair.getValue());
    return new ImmutableHashTable<>(proxy);
  }

  public static <K, V> ImmutableHashTable<K, V> copyOf(Map<? extends K, ? extends V> map) {
    return new ImmutableHashTable<>(new HashMap<>(map));
  }

  @Override
  public Option<V> tryGetValue(K key) {
    return Option.fromNullable(proxy.get(key));
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
