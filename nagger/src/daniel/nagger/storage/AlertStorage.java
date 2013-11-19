package daniel.nagger.storage;

import daniel.bdb.SerializingDatabase;
import daniel.data.collection.Collection;
import daniel.data.option.Option;
import daniel.data.serialization.GsonSerializer;
import daniel.data.serialization.StringSerializer;
import daniel.nagger.Config;
import daniel.nagger.model.Alert;

public class AlertStorage {
  private static final SerializingDatabase<String, Alert> byUuid =
      new SerializingDatabase<String, Alert>(
          Config.getDatabaseHome("alerts"),
          StringSerializer.singleton,
          new GsonSerializer<Alert>(Alert.class));

  public static void saveNewAlert(Alert alert) {
    byUuid.put(alert.uuid, alert);
  }

  public static void updateAlert(Alert alert) {
    byUuid.put(alert.uuid, alert);
  }

  public static void deleteAlert(Alert alert) {
    byUuid.delete(alert.uuid);
  }

  public static void deleteAll() {
    for (Alert alert : getAllAlerts())
      deleteAlert(alert);
  }

  public static Option<Alert> getAlertByUuid(String uuid) {
    return byUuid.get(uuid);
  }

  public static Collection<Alert> getAllAlerts() {
    return byUuid.getAllValues();
  }
}
