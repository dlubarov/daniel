package daniel.data.multidictionary.sequential;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.sequence.AbstractSequence;

public abstract class AbstractSequentialMultidictionary<K, V>
    extends AbstractSequence<KeyValuePair<K, V>>
    implements SequentialMultidictionary<K, V> {
  @Override
  public ImmutableSequentialMultidictionary<K, V> toImmutable() {
    return ImmutableArrayMultidictionary.copyOf(this);
  }
}
