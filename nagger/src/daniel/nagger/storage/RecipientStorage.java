package daniel.nagger.storage;

import daniel.bdb.SerializingDatabase;
import daniel.data.collection.Collection;
import daniel.data.option.Option;
import daniel.data.serialization.GsonSerializer;
import daniel.data.serialization.StringSerializer;
import daniel.nagger.Config;
import daniel.nagger.model.Recipient;

public class RecipientStorage {
  private static final SerializingDatabase<String, Recipient> byUuid =
      new SerializingDatabase<String, Recipient>(
          Config.getDatabaseHome("recipients"),
          StringSerializer.singleton,
          new GsonSerializer<Recipient>(Recipient.class));

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
