package daniel.chat;

import com.google.gson.Gson;
import daniel.data.table.MutableHashTable;
import daniel.logging.Logger;
import daniel.web.http.server.WebSocketHandler;
import daniel.web.http.server.WebSocketManager;
import daniel.web.http.websocket.WebSocketFrame;
import daniel.web.http.websocket.WebSocketMessage;
import daniel.web.http.websocket.WebSocketOpcode;
import java.nio.charset.StandardCharsets;

public final class ChatWebSocketHandler implements WebSocketHandler {
  private static final Logger logger = Logger.forClass(ChatWebSocketHandler.class);
  private static final Gson gson = new Gson();

  public static final ChatWebSocketHandler singleton = new ChatWebSocketHandler();

  // TODO: Should be multi-map? (Or just Set<WebSocketManager>.)
  private final MutableHashTable<String, WebSocketManager> managersByChannel;

  private ChatWebSocketHandler() {
    this.managersByChannel = MutableHashTable.create();
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
    String rawMessageString = new String(message.getData(), StandardCharsets.UTF_8);
    ChatMessage parsedMessage = gson.fromJson(rawMessageString, ChatMessage.class);
    logger.info("Channel %s received message: %s", getChannel(manager), parsedMessage);
    WebSocketFrame frame = new WebSocketFrame.Builder()
        .setFinalFragment(true)
        .setOpcode(WebSocketOpcode.TEXT_FRAME)
        .setPayload(gson.toJson(parsedMessage).getBytes(StandardCharsets.UTF_8))
        .build();
    for (WebSocketManager peer : managersByChannel.getValues(getChannel(manager)))
      peer.send(frame);
  }

  private static String getChannel(WebSocketManager manager) {
    return manager.getRequest().getResource();
  }
}
