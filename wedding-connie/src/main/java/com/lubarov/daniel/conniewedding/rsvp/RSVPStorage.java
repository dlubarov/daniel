package com.lubarov.daniel.conniewedding.rsvp;

import com.lubarov.daniel.bdb.SerializingDatabase;
import com.lubarov.daniel.conniewedding.WeddingConfig;
import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.serialization.StringSerializer;

public final class RSVPStorage {
  private static final SerializingDatabase<String, RSVP> byUuid = new SerializingDatabase<>(
      WeddingConfig.getDatabaseHome("rsvps"), StringSerializer.singleton, RSVPSerializer.singleton);

  private RSVPStorage() {}

  public static void saveNewRSVP(RSVP rsvp) {
    byUuid.put(rsvp.uuid, rsvp);
  }

  public static Collection<RSVP> getAllRSVPs() {
    return byUuid.getAllValues();
  }
}
