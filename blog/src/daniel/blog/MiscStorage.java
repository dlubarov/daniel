package daniel.blog;

import daniel.bdb.SerializingDatabase;
import daniel.data.option.Option;
import daniel.data.serialization.StringSerializer;

public final class MiscStorage {
  private static final SerializingDatabase<String, String> database = new SerializingDatabase<>(
      Config.getDatabaseHome("misc"), StringSerializer.singleton, StringSerializer.singleton);

  private MiscStorage() {}

  public static Option<String> tryGetAdminPassword() {
    return database.get("admin_password");
  }

  public static void setAdminPassword(String adminPassword) {
    database.put("admin_password", adminPassword);
  }
}
