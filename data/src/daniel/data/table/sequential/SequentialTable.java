package daniel.data.table.sequential;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.sequence.Sequence;
import daniel.data.table.Table;

public interface SequentialTable<K, V>
    extends Table<K, V>, Sequence<KeyValuePair<K, V>> {
  @Override
  public ImmutableSequentialTable<K, V> toImmutable();
}
