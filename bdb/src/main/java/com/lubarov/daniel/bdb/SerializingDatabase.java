package com.lubarov.daniel.bdb;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.serialization.Serializer;

import java.io.File;

public final class SerializingDatabase<K, V> {
  private final RawDatabase rawDatabase;
  private final Serializer<K> keySerializer;
  private final Serializer<V> valueSerializer;

  public SerializingDatabase(File envHome, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
    this.rawDatabase = new RawDatabase(envHome);
    this.keySerializer = keySerializer;
    this.valueSerializer = valueSerializer;
  }

  public SerializingDatabase(String envHome, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
    this.rawDatabase = new RawDatabase(envHome);
    this.keySerializer = keySerializer;
    this.valueSerializer = valueSerializer;
  }

  public Option<V> get(K key) {
    return rawDatabase.tryGet(keySerializer.writeToByteArray(key))
        .map(valueSerializer::readFromByteArray);
  }

  public void put(K key, V value) {
    rawDatabase.put(keySerializer.writeToByteArray(key), valueSerializer.writeToByteArray(value));
  }

  public boolean delete(K key) {
    return rawDatabase.delete(keySerializer.writeToByteArray(key));
  }

  public Sequence<K> getAllKeys() {
    return rawDatabase.getAllKeys().map(keySerializer::readFromByteArray);
  }

  public Sequence<V> getAllValues() {
    return rawDatabase.getAllValues().map(valueSerializer::readFromByteArray);
  }
}
