package com.lubarov.daniel.data.dictionary;

public abstract class AbstractImmutableDictionary<K, V>
    extends AbstractDictionary<K, V>
    implements ImmutableDictionary<K, V> {
  @Override
  public ImmutableDictionary<K, V> toImmutable() {
    return this;
  }
}
