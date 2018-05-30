package com.lubarov.daniel.web.http.server;

import com.lubarov.daniel.web.http.websocket.WebSocketMessage;

public interface WebSocketHandler {
  void onConnect(WebSocketManager manager);

  void onDisconnect(WebSocketManager manager);

  void handle(WebSocketManager manager, WebSocketMessage message);
}
