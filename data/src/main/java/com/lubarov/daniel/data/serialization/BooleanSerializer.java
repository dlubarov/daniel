package com.lubarov.daniel.data.serialization;

public final class BooleanSerializer extends AbstractSerializer<Boolean> {
  public static final BooleanSerializer singleton = new BooleanSerializer();

  private BooleanSerializer() {}

  @Override
  public void writeToSink(Boolean b, ByteSink sink) {
    sink.give(b ? (byte) 1 : (byte) 0);
  }

  @Override
  public Boolean readFromSource(ByteSource source) {
    switch (source.take()) {
      case 0:
        return false;
      case 1:
        return true;
      default:
        throw new RuntimeException("Unexpected byte.");
    }
  }
}
