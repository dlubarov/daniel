package com.lubarov.daniel.data.dictionary;

import com.lubarov.daniel.data.util.EqualsBuilder;

import java.util.Map;

public final class KeyValuePair<K, V> {
  private final K key;
  private final V value;

  public KeyValuePair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public KeyValuePair(Map.Entry<K, V> mapEntry) {
    this(mapEntry.getKey(), mapEntry.getValue());
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public Map.Entry<K, V> toMapEntry() {
    return new java.util.AbstractMap.SimpleEntry<>(key, value);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof KeyValuePair<?, ?>)) {
      return false;
    }

    KeyValuePair<?, ?> that = (KeyValuePair<?, ?>) o;
    return new EqualsBuilder()
        .append(key, that.key)
        .append(value, that.value)
        .isEquals();
  }

  @Override
  public String toString() {
    return String.format("%s -> %s", key, value);
  }
}
