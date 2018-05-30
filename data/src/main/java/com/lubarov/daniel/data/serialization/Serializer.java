package com.lubarov.daniel.data.serialization;

public interface Serializer<A> {
  void writeToSink(A object, ByteSink sink);

  A readFromSource(ByteSource source);

  byte[] writeToByteArray(A object);

  A readFromByteArray(byte[] data);
}
