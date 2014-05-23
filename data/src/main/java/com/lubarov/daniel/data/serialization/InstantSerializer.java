package com.lubarov.daniel.data.serialization;

import com.lubarov.daniel.data.unit.Instant;

public final class InstantSerializer extends AbstractSerializer<Instant> {
  public static final InstantSerializer singleton = new InstantSerializer();

  private InstantSerializer() {}

  @Override
  public void writeToSink(Instant instant, ByteSink sink) {
    DateSerializer.singleton.writeToSink(instant.toDate(), sink);
  }

  @Override
  public Instant readFromSource(ByteSource source) {
    return Instant.fromDate(DateSerializer.singleton.readFromSource(source));
  }
}
