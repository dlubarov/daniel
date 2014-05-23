package com.lubarov.daniel.nagger.storage;

import com.lubarov.daniel.bdb.SerializingDatabase;
import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.serialization.GsonSerializer;
import com.lubarov.daniel.data.serialization.StringSerializer;
import com.lubarov.daniel.nagger.Config;
import com.lubarov.daniel.nagger.model.Recipient;

public class RecipientStorage {
  private static final SerializingDatabase<String, Recipient> byUuid =
      new SerializingDatabase<>(
          Config.getDatabaseHome("recipients"),
          StringSerializer.singleton,
          new GsonSerializer<>(Recipient.class));

  public static void saveNewRecipient(Recipient recipient) {
    byUuid.put(recipient.uuid, recipient);
  }

  public static void updateRecipient(Recipient recipient) {
    byUuid.put(recipient.uuid, recipient);
  }

  public static void deleteRecipient(Recipient recipient) {
    byUuid.delete(recipient.uuid);
  }

  public static void deleteAll() {
    for (Recipient recipient : getAllRecipients())
      deleteRecipient(recipient);
  }

  public static Option<Recipient> getRecipientByUuid(String uuid) {
    return byUuid.get(uuid);
  }

  public static Collection<Recipient> getAllRecipients() {
    return byUuid.getAllValues();
  }
}
