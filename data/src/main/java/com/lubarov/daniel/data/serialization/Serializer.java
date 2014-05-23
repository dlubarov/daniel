package com.lubarov.daniel.data.serialization;

public interface Serializer<A> {
  public void writeToSink(A object, ByteSink sink);

  public A readFromSource(ByteSource source);

  public byte[] writeToByteArray(A object);

  public A readFromByteArray(byte[] data);
}
