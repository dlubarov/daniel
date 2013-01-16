package daniel.data.multidictionary.sequential;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.multidictionary.ImmutableMultidictionary;
import daniel.data.sequence.ImmutableSequence;

public interface ImmutableSequentialMultidictionary<K, V>
    extends SequentialMultidictionary<K, V>, ImmutableMultidictionary<K, V>,
    ImmutableSequence<KeyValuePair<K, V>> {
  @Override
  public ImmutableSequentialMultidictionary<K, V> toImmutable();
}
