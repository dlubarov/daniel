package daniel.data.table.sequential;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.table.ImmutableTable;

/**
 * A {@link SequentialTable} whose elements never change.
 */
public interface ImmutableSequentialTable<K, V>
    extends SequentialTable<K, V>, ImmutableTable<K, V>,
    ImmutableSequence<KeyValuePair<K, V>> {
  @Override
  public ImmutableSequentialTable<K, V> toImmutable();
}
