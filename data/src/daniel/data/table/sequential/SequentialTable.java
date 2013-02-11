package daniel.data.table.sequential;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.table.Table;
import daniel.data.sequence.Sequence;

public interface SequentialTable<K, V>
    extends Table<K, V>, Sequence<KeyValuePair<K, V>> {
  @Override
  public ImmutableSequentialTable<K, V> toImmutable();
}
