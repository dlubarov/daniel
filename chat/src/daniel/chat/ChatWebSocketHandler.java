package daniel.chat;

import daniel.data.multidictionary.MutableHashMultitable;
import daniel.logging.Logger;
import daniel.web.http.server.WebSocketHandler;
import daniel.web.http.server.WebSocketManager;
import daniel.web.http.websocket.WebSocketFrame;
import daniel.web.http.websocket.WebSocketMessage;
import daniel.web.http.websocket.WebSocketOpcode;
import java.nio.charset.StandardCharsets;

public final class ChatWebSocketHandler implements WebSocketHandler {
  private static final Logger logger = Logger.forClass(ChatWebSocketHandler.class);

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
  public synchronized void onDisconnect(WebSocketManager manager) {
    if (!managersByChannel.tryRemove(getChannel(manager), manager))
      logger.warn("Disconnected user not found.");
  }

  @Override
  public synchronized void handle(WebSocketManager manager, WebSocketMessage message) {
    String messageString = new String(message.getData(), StandardCharsets.UTF_8);
    logger.info("Channel %s received message: %s", getChannel(manager), messageString);
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
