package daniel.data.dictionary;

import daniel.data.dictionary.functions.MapEntryConverter;
import daniel.data.option.Option;
import daniel.data.source.IteratorSource;
import daniel.data.source.Source;
import java.util.HashMap;
import java.util.Map;

public final class ImmutableHashDictionary<K, V> extends AbstractImmutableDictionary<K, V> {
  private final HashMap<K, V> proxy;

  private ImmutableHashDictionary(HashMap<K, V> proxy) {
    this.proxy = proxy;
  }

  public static <K, V> ImmutableHashDictionary<K, V> create() {
    return new ImmutableHashDictionary<>(new HashMap<K, V>());
  }

  public static <K, V> ImmutableHashDictionary<K, V> copyOf(Map<? extends K, ? extends V> map) {
    return new ImmutableHashDictionary<>(new HashMap<>(map));
  }

  public static <K, V> ImmutableHashDictionary<K, V> copyOf(Iterable<KeyValuePair<K, V>> keyValuePairs) {
    HashMap<K, V> proxy = new HashMap<>();
    for (KeyValuePair<? extends K, ? extends V> keyValuePair : keyValuePairs)
      proxy.put(keyValuePair.getKey(), keyValuePair.getValue());
    return new ImmutableHashDictionary<>(proxy);
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
