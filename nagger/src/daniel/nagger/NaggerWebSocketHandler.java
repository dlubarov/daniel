package daniel.nagger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daniel.data.set.MutableHashSet;
import daniel.logging.Logger;
import daniel.nagger.messages.c2s.C2sMessage;
import daniel.nagger.messages.s2c.S2cMessage;
import daniel.web.http.server.WebSocketHandler;
import daniel.web.http.server.WebSocketManager;
import daniel.web.http.websocket.WebSocketFrame;
import daniel.web.http.websocket.WebSocketMessage;
import daniel.web.http.websocket.WebSocketOpcode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class NaggerWebSocketHandler implements WebSocketHandler {
  public static final NaggerWebSocketHandler singleton = new NaggerWebSocketHandler();

  private static final Logger logger = Logger.forClass(NaggerWebSocketHandler.class);

  private static final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
      .create();
  public static final Charset CHARSET = StandardCharsets.US_ASCII;

  private final MutableHashSet<WebSocketManager> activeClients;

  private NaggerWebSocketHandler() {
    activeClients = MutableHashSet.create();
  }

  @Override
  public void onConnect(WebSocketManager manager) {
    if (!activeClients.tryAdd(manager))
      logger.warn("Duplicate WebSocket manager: %s.", manager);
  }

  @Override
  public void onDisconnect(WebSocketManager manager) {
    if (!activeClients.tryRemove(manager))
      logger.warn("WebSocket manager not found: %s.", manager);
  }

  @Override
  public void handle(WebSocketManager manager, WebSocketMessage message) {
    C2sMessage c2sMessage = gson.fromJson(new String(message.getData(), CHARSET), C2sMessage.class);
    // TODO handle message
    logger.warn("Unexpected incoming message %s from %s.", c2sMessage, manager);
  }

  public void broadcast(S2cMessage s2cMessage) {
    for (WebSocketManager client : activeClients)
      client.send(new WebSocketFrame.Builder()
          .setFinalFragment(true)
          .setOpcode(WebSocketOpcode.TEXT_FRAME)
          .setPayload(gson.toJson(s2cMessage).getBytes(CHARSET))
          .build());
  }
}
