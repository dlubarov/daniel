package com.lubarov.daniel.multiweb;

import com.lubarov.daniel.chat.ChatWebSocketHandler;
import com.lubarov.daniel.nagger.NaggerWebSocketHandler;
import com.lubarov.daniel.web.http.server.WebSocketHandler;
import com.lubarov.daniel.web.http.server.util.HostBasedWebSocketHandler;

final class MultiwebWebSocketHandler {
  private MultiwebWebSocketHandler() {}

  public static WebSocketHandler getHandler() {
    return new HostBasedWebSocketHandler.Builder()
        .addHandlerForHost(".*nagger\\.daniel\\.lubarov\\.com.*", NaggerWebSocketHandler.singleton)
        .addHandlerForHost(".*jabberings\\.net.*", ChatWebSocketHandler.singleton)
        .build();
  }
}
