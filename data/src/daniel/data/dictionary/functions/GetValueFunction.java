package daniel.data.dictionary.functions;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.function.Function;

public final class GetValueFunction<K, V> implements Function<KeyValuePair<K, V>, V> {
  @Override
  public V apply(KeyValuePair<K, V> keyValuePair) {
    return keyValuePair.getValue();
  }
}
