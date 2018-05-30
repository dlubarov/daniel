package com.lubarov.daniel.data.table.sequential;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.table.Table;

/**
 * A {@link Table} with a consistent, defined order of enumeration.
 */
public interface SequentialTable<K, V>
    extends Table<K, V>, Sequence<KeyValuePair<K, V>> {
  @Override
  ImmutableSequentialTable<K, V> toImmutable();
}
