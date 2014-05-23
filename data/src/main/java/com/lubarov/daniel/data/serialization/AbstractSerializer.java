package com.lubarov.daniel.data.serialization;

public abstract class AbstractSerializer<A> implements Serializer<A> {
  @Override
  public byte[] writeToByteArray(A object) {
    ByteSink sink = new ByteSink();
    writeToSink(object, sink);
    return sink.getBytes();
  }

  @Override
  public A readFromByteArray(byte[] data) {
    return readFromSource(new ByteSource(data));
  }
}
