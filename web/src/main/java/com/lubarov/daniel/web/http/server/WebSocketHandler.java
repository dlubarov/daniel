package com.lubarov.daniel.web.http.server;

import com.lubarov.daniel.web.http.websocket.WebSocketMessage;

public interface WebSocketHandler {
  public void onConnect(WebSocketManager manager);

  public void onDisconnect(WebSocketManager manager);

  public void handle(WebSocketManager manager, WebSocketMessage message);
}
