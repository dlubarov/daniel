package daniel.web.http.server;

import daniel.web.http.HttpRequest;
import daniel.web.http.websocket.WebSocketMessage;

public interface WebSocketHandler {
  public void handle(WebSocketMessage message, HttpRequest originalRequest);
}
