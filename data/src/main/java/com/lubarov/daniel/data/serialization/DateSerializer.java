package com.lubarov.daniel.data.serialization;

import java.util.Date;

public final class DateSerializer extends AbstractSerializer<Date> {
  public static final DateSerializer singleton = new DateSerializer();

  private DateSerializer() {}

  @Override
  public void writeToSink(Date date, ByteSink sink) {
    LongSerializer.singleton.writeToSink(date.getTime(), sink);
  }

  @Override
  public Date readFromSource(ByteSource source) {
    return new Date(LongSerializer.singleton.readFromSource(source));
  }
}
