package daniel.cms.session;

import daniel.bdb.SerializingDatabase;
import daniel.cms.Config;
import daniel.data.option.Option;
import daniel.data.serialization.GsonSerializer;
import daniel.data.serialization.StringSerializer;

public final class SessionStorage {
  private static final SerializingDatabase<String, Session> byUuid =
      new SerializingDatabase<>(Config.getDatabaseHome("sessions"),
          StringSerializer.singleton, new GsonSerializer<>(Session.class));

  private SessionStorage() {}

  public static synchronized Option<Session> getSessionByUuid(String uuid) {
    return byUuid.get(uuid);
  }

  public static synchronized void saveNewSession(Session session) {
    byUuid.put(session.getUuid(), session);
  }

  public static synchronized void updateSession(Session session) {
    byUuid.put(session.getUuid(), session);
  }
}
