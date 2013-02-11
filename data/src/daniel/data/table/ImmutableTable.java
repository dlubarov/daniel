package daniel.data.table;

import daniel.data.collection.ImmutableCollection;
import daniel.data.dictionary.KeyValuePair;

public interface ImmutableTable<K, V>
    extends Table<K, V>, ImmutableCollection<KeyValuePair<K, V>> {}
