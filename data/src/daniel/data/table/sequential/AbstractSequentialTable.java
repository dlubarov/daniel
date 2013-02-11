package daniel.data.table.sequential;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.sequence.AbstractSequence;

public abstract class AbstractSequentialTable<K, V>
    extends AbstractSequence<KeyValuePair<K, V>>
    implements SequentialTable<K, V> {
  @Override
  public ImmutableSequentialTable<K, V> toImmutable() {
    return ImmutableArrayTable.copyOf(this);
  }
}
