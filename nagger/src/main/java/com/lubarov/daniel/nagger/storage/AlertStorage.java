package com.lubarov.daniel.nagger.storage;

import com.lubarov.daniel.bdb.SerializingDatabase;
import com.lubarov.daniel.common.Environment;
import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.serialization.GsonSerializer;
import com.lubarov.daniel.data.serialization.StringSerializer;
import com.lubarov.daniel.nagger.Config;
import com.lubarov.daniel.nagger.model.Alert;

public class AlertStorage {
  private static volatile boolean locked = false;

  private static final SerializingDatabase<String, Alert> byUuid =
      new SerializingDatabase<>(
          Config.getDatabaseHome("alerts"),
          StringSerializer.singleton,
          new GsonSerializer<>(Alert.class));

  public static void saveNewAlert(Alert alert) {
    failIfLocked();
    byUuid.put(alert.uuid, alert);
  }

  public static void updateAlert(Alert alert) {
    failIfLocked();
    byUuid.put(alert.uuid, alert);
  }

  public static void deleteAlert(Alert alert) {
    failIfLocked();
    byUuid.delete(alert.uuid);
  }

  public static void deleteAll() {
    failIfLocked();
    for (Alert alert : getAllAlerts())
      deleteAlert(alert);
  }

  public static Option<Alert> getAlertByUuid(String uuid) {
    return byUuid.get(uuid);
  }

  public static Collection<Alert> getAllAlerts() {
    return byUuid.getAllValues();
  }

  public static void lock() {
    locked = true;
  }

  private static void failIfLocked() {
    if (locked) {
      throw new RuntimeException("Alert storage is locked");
    }
  }
}
