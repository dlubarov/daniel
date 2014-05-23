package com.lubarov.daniel.data.dictionary.functions;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.function.Function;

public final class GetKeyFunction<K, V> implements Function<KeyValuePair<K, V>, K> {
  @Override
  public K apply(KeyValuePair<K, V> keyValuePair) {
    return keyValuePair.getKey();
  }
}
