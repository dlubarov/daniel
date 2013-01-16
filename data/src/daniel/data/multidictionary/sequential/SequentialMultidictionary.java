package daniel.data.multidictionary.sequential;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.multidictionary.Multidictionary;
import daniel.data.sequence.Sequence;

public interface SequentialMultidictionary<K, V>
    extends Multidictionary<K, V>, Sequence<KeyValuePair<K, V>> {
  @Override
  public ImmutableSequentialMultidictionary<K, V> toImmutable();
}
