package com.lubarov.daniel.conniewedding.rsvp;

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
    StringSerializer.singleton.writeToSink(rsvp.emailOrPhone, sink);
    BooleanSerializer.singleton.writeToSink(rsvp.attending, sink);
    StringSerializer.singleton.writeToSink(rsvp.partySize, sink);
    StringSerializer.singleton.writeToSink(rsvp.entrees, sink);
    StringSerializer.singleton.writeToSink(rsvp.kids, sink);
  }

  @Override
  public RSVP readFromSource(ByteSource source) {
    return new RSVP.Builder()
        .setUUID(StringSerializer.singleton.readFromSource(source))
        .setCreatedAt(InstantSerializer.singleton.readFromSource(source))
        .setName(StringSerializer.singleton.readFromSource(source))
        .setEmailOrPhone(StringSerializer.singleton.readFromSource(source))
        .setAttending(BooleanSerializer.singleton.readFromSource(source))
        .setPartySize(StringSerializer.singleton.readFromSource(source))
        .setEntrees(StringSerializer.singleton.readFromSource(source))
        .setKids(StringSerializer.singleton.readFromSource(source))
        .build();
  }
}
