package com.lubarov.daniel.data.table.sequential;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.sequence.AbstractSequence;

public abstract class AbstractSequentialTable<K, V>
    extends AbstractSequence<KeyValuePair<K, V>>
    implements SequentialTable<K, V> {
  @Override
  public ImmutableSequentialTable<K, V> toImmutable() {
    return ImmutableArrayTable.copyOf(this);
  }
}
