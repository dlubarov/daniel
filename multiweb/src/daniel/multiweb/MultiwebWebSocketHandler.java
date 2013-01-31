package daniel.multiweb;

import daniel.chat.ChatWebSocketHandler;
import daniel.web.http.server.WebSocketHandler;
import daniel.web.http.server.util.HostBasedWebSocketHandler;

public final class MultiwebWebSocketHandler {
  private MultiwebWebSocketHandler() {}

  public static WebSocketHandler getHandler() {
    return new HostBasedWebSocketHandler.Builder()
        .addHandlerForHost(".*jabberings\\.net.*", ChatWebSocketHandler.singleton)
        .build();
  }
}
