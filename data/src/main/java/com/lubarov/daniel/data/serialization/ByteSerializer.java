package com.lubarov.daniel.data.serialization;

public final class ByteSerializer extends AbstractSerializer<Byte> {
  public static final ByteSerializer singleton = new ByteSerializer();

  private ByteSerializer() {}

  @Override
  public void writeToSink(Byte b, ByteSink sink) {
    sink.give(b);
  }

  @Override
  public Byte readFromSource(ByteSource source) {
    return source.take();
  }
}
