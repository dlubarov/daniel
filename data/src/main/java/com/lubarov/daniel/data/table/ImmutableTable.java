package com.lubarov.daniel.data.table;

import com.lubarov.daniel.data.collection.ImmutableCollection;
import com.lubarov.daniel.data.dictionary.KeyValuePair;

/**
 * A {@link Table} whose elements never change.
 */
public interface ImmutableTable<K, V>
    extends Table<K, V>, ImmutableCollection<KeyValuePair<K, V>> {}
