package daniel.data.dictionary.functions;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.function.Function;

public final class GetKeyFunction<K, V> implements Function<KeyValuePair<K, V>, K> {
  @Override
  public K apply(KeyValuePair<K, V> keyValuePair) {
    return keyValuePair.getKey();
  }
}
