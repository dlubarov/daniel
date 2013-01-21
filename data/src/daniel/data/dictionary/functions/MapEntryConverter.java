package daniel.data.dictionary.functions;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.function.Function;
import java.util.Map;

public final class MapEntryConverter<K, V>
    implements Function<Map.Entry<K, V>, KeyValuePair<K, V>> {
  @Override
  public KeyValuePair<K, V> apply(Map.Entry<K, V> mapEntry) {
    return new KeyValuePair<>(mapEntry);
  }
}
