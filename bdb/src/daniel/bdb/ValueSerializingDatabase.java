package daniel.bdb;

import daniel.data.sequence.Sequence;
import daniel.data.serialization.Serializer;
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

  public V get(byte[] key) {
    return valueSerializer.readFromByteArray(rawDatabase.get(key));
  }

  public void put(byte[] key, V value) {
    rawDatabase.put(key, valueSerializer.writeToByteArray(value));
  }

  public Sequence<byte[]> getAllKeys() {
    return rawDatabase.getAllKeys();
  }
}
