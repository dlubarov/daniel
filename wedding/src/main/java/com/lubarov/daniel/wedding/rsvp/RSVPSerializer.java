package com.lubarov.daniel.wedding.rsvp;

import com.lubarov.daniel.data.serialization.AbstractSerializer;
import com.lubarov.daniel.data.serialization.BooleanSerializer;
import com.lubarov.daniel.data.serialization.ByteSink;
import com.lubarov.daniel.data.serialization.ByteSource;
import com.lubarov.daniel.data.serialization.InstantSerializer;
import com.lubarov.daniel.data.serialization.StringSerializer;

public class RSVPSerializer extends AbstractSerializer<RSVP> {
  public static final RSVPSerializer singleton = new RSVPSerializer();

  private RSVPSerializer() {}

  @Override
  public void writeToSink(RSVP rsvp, ByteSink sink) {
    StringSerializer.singleton.writeToSink(rsvp.uuid, sink);
    InstantSerializer.singleton.writeToSink(rsvp.createdAt, sink);
    StringSerializer.singleton.writeToSink(rsvp.name, sink);
    StringSerializer.singleton.writeToSink(rsvp.email, sink);
    BooleanSerializer.singleton.writeToSink(rsvp.attending, sink);
    BooleanSerializer.singleton.writeToSink(rsvp.guestAttending, sink);
    StringSerializer.singleton.writeToSink(rsvp.guestName, sink);
    StringSerializer.singleton.writeToSink(rsvp.comments, sink);
  }

  @Override
  public RSVP readFromSource(ByteSource source) {
    return new RSVP.Builder()
        .setUUID(StringSerializer.singleton.readFromSource(source))
        .setCreatedAt(InstantSerializer.singleton.readFromSource(source))
        .setName(StringSerializer.singleton.readFromSource(source))
        .setEmail(StringSerializer.singleton.readFromSource(source))
        .setAttending(BooleanSerializer.singleton.readFromSource(source))
        .setGuestAttending(BooleanSerializer.singleton.readFromSource(source))
        .setGuestName(StringSerializer.singleton.readFromSource(source))
        .setComments(StringSerializer.singleton.readFromSource(source))
        .build();
  }
}
