package com.lubarov.daniel.data.dictionary;

import com.lubarov.daniel.data.dictionary.functions.MapEntryConverter;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.source.IteratorSource;
import com.lubarov.daniel.data.source.Source;

import java.util.HashMap;
import java.util.Map;

public final class MutableHashDictionary<K, V> extends AbstractDictionary<K, V> {
  private final HashMap<K, V> proxy;

  private MutableHashDictionary(HashMap<K, V> proxy) {
    this.proxy = proxy;
  }

  public static <K, V> MutableHashDictionary<K, V> create() {
    return new MutableHashDictionary<>(new HashMap<>());
  }

  public static <K, V> MutableHashDictionary<K, V> copyOf(
      Iterable<KeyValuePair<? extends K, ? extends V>> keyValuePairs) {
    HashMap<K, V> proxy = new HashMap<>();
    for (KeyValuePair<? extends K, ? extends V> keyValuePair : keyValuePairs)
      proxy.put(keyValuePair.getKey(), keyValuePair.getValue());
    return new MutableHashDictionary<>(proxy);
  }

  public static <K, V> MutableHashDictionary<K, V> copyOf(Map<? extends K, ? extends V> map) {
    return new MutableHashDictionary<>(new HashMap<>(map));
  }

  @Override
  public Option<V> tryGetValue(K key) {
    return Option.fromNullable(proxy.get(key));
  }

  public void put(K key, V value) {
    proxy.put(key, value);
  }

  public boolean tryRemove(K key) {
    return proxy.remove(key) != null;
  }

  @Override
  public Source<KeyValuePair<K, V>> getEnumerator() {
    return new IteratorSource<>(proxy.entrySet().iterator()).map(new MapEntryConverter<>());
  }

  @Override
  public int getSize() {
    return proxy.size();
  }
}
