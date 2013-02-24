package daniel.web.http;

import daniel.data.dictionary.MutableHashDictionary;
import daniel.data.option.Option;

/**
 * Stores session data in memory. This data will be lost when the JVM shuts down.
 */
public final class MemorySessionData<A> {
  private final MutableHashDictionary<String, A> dataBySessionId = MutableHashDictionary.create();

  public synchronized void set(HttpRequest request, A value) {
    dataBySessionId.put(SessionGenerator.getSessionId(request), value);
  }

  public synchronized Option<A> tryGet(HttpRequest request) {
    return dataBySessionId.tryGetValue(SessionGenerator.getSessionId(request));
  }

  public synchronized boolean tryClear(HttpRequest request) {
    return dataBySessionId.tryRemove(SessionGenerator.getSessionId(request));
  }
}
