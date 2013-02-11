package daniel.data.table;

import daniel.data.collection.AbstractCollection;
import daniel.data.dictionary.KeyValuePair;

public abstract class AbstractTable<K, V>
    extends AbstractCollection<KeyValuePair<K, V>>
    implements Table<K, V> {
  @Override
  public ImmutableTable<K, V> toImmutable() {
    return ImmutableHashTable.copyOf(this);
  }
}
