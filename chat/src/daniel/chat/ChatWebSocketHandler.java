package daniel.chat;

import daniel.data.multidictionary.MutableHashMultitable;
import daniel.web.http.server.WebSocketHandler;
import daniel.web.http.server.WebSocketManager;
import daniel.web.http.websocket.WebSocketFrame;
import daniel.web.http.websocket.WebSocketMessage;
import daniel.web.http.websocket.WebSocketOpcode;

public final class ChatWebSocketHandler implements WebSocketHandler {
  public static final ChatWebSocketHandler singleton = new ChatWebSocketHandler();

  private final MutableHashMultitable<String, WebSocketManager> managersByChannel;

  private ChatWebSocketHandler() {
    this.managersByChannel = MutableHashMultitable.create();
  }

  @Override
  public synchronized void onConnect(WebSocketManager manager) {
    managersByChannel.put(getChannel(manager), manager);
  }

  @Override
  public synchronized void handle(WebSocketManager manager, WebSocketMessage message) {
    WebSocketFrame frame = new WebSocketFrame.Builder()
        .setFinalFragment(true)
        .setOpcode(WebSocketOpcode.TEXT_FRAME)
        .setPayload(message.getData())
        .build();
    for (WebSocketManager peer : managersByChannel.getValues(getChannel(manager)))
      peer.send(frame);
  }

  private static String getChannel(WebSocketManager manager) {
    return manager.getRequest().getResource();
  }
}
