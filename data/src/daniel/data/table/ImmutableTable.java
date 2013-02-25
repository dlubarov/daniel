package daniel.data.table;

import daniel.data.collection.ImmutableCollection;
import daniel.data.dictionary.KeyValuePair;

/**
 * A {@link Table} whose elements never change.
 */
public interface ImmutableTable<K, V>
    extends Table<K, V>, ImmutableCollection<KeyValuePair<K, V>> {}
