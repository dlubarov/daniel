package daniel.web.http.server;

import daniel.web.http.websocket.WebSocketMessage;

public interface WebSocketHandler {
  public void onConnect(WebSocketManager manager);

  public void handle(WebSocketManager manager, WebSocketMessage message);
}
