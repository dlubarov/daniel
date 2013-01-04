package daniel.bdb;

import daniel.data.function.Function;
import daniel.data.sequence.Sequence;
import daniel.data.serialization.Serializer;
import java.io.File;

public class SerializingDatabase<K, V> {
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

  public V get(K key) {
    return valueSerializer.readFromByteArray(rawDatabase.get(keySerializer.writeToByteArray(key)));
  }

  public void put(K key, V value) {
    rawDatabase.put(keySerializer.writeToByteArray(key), valueSerializer.writeToByteArray(value));
  }

  public Sequence<K> getAllKeys() {
    return rawDatabase.getAllKeys().map(new Function<byte[], K>() {
      @Override public K apply(byte[] rawKey) {
        return keySerializer.readFromByteArray(rawKey);
      }
    });
  }

  public Sequence<V> getAllValues() {
    return rawDatabase.getAllValues().map(new Function<byte[], V>() {
      @Override public V apply(byte[] rawValue) {
        return valueSerializer.readFromByteArray(rawValue);
      }
    });
  }
}
