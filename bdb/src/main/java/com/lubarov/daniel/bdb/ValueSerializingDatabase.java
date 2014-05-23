package com.lubarov.daniel.bdb;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.serialization.Serializer;

import java.io.File;

public final class ValueSerializingDatabase<V> {
  private final RawDatabase rawDatabase;
  private final Serializer<V> valueSerializer;

  public ValueSerializingDatabase(File envHome, Serializer<V> valueSerializer) {
    this.rawDatabase = new RawDatabase(envHome);
    this.valueSerializer = valueSerializer;
  }

  public ValueSerializingDatabase(String envHome, Serializer<V> valueSerializer) {
    this.rawDatabase = new RawDatabase(envHome);
    this.valueSerializer = valueSerializer;
  }

  public Option<V> get(byte[] key) {
    return rawDatabase.tryGet(key).map(valueSerializer::readFromByteArray);
  }

  public boolean delete(byte[] key) {
    return rawDatabase.delete(key);
  }

  public void put(byte[] key, V value) {
    rawDatabase.put(key, valueSerializer.writeToByteArray(value));
  }

  public Sequence<byte[]> getAllKeys() {
    return rawDatabase.getAllKeys();
  }
}
