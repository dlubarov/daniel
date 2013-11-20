package daniel.nagger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daniel.data.set.MutableHashSet;
import daniel.logging.Logger;
import daniel.nagger.messages.c2s.C2sAddTagMessage;
import daniel.nagger.messages.c2s.C2sCreateAlertMessage;
import daniel.nagger.messages.c2s.C2sEditAlertCommandMessage;
import daniel.nagger.messages.c2s.C2sEditAlertDescriptionMessage;
import daniel.nagger.messages.c2s.C2sEditAlertNameMessage;
import daniel.nagger.messages.c2s.C2sMessage;
import daniel.nagger.messages.s2c.S2cCreateAlertMessage;
import daniel.nagger.messages.s2c.S2cEditAlertCommandMessage;
import daniel.nagger.messages.s2c.S2cEditAlertDescriptionMessage;
import daniel.nagger.messages.s2c.S2cEditAlertNameMessage;
import daniel.nagger.messages.s2c.S2cMessage;
import daniel.nagger.model.Alert;
import daniel.nagger.storage.AlertStorage;
import daniel.web.http.server.WebSocketHandler;
import daniel.web.http.server.WebSocketManager;
import daniel.web.http.websocket.WebSocketFrame;
import daniel.web.http.websocket.WebSocketMessage;
import daniel.web.http.websocket.WebSocketOpcode;
import daniel.web.util.UuidUtils;
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
    if (c2sMessage.createAlert != null)
      handle(c2sMessage.createAlert);
    if (c2sMessage.addTag != null)
      handle(c2sMessage.addTag);
    if (c2sMessage.editAlertName != null)
      handle(c2sMessage.editAlertName);
    if (c2sMessage.editAlertDescription != null)
      handle(c2sMessage.editAlertDescription);
    if (c2sMessage.editAlertCommand != null)
      handle(c2sMessage.editAlertCommand);
  }

  private void handle(C2sCreateAlertMessage createAlert) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = createAlert.name;
    alert.description = createAlert.description;
    alert.command = createAlert.command;
    alert.frequency = createAlert.frequency;
    alert.recipientUuids = createAlert.recipientUuids;
    alert.tags = createAlert.tags;

    AlertStorage.saveNewAlert(alert);
    AlertProcessor.singleton.startProcessing(alert.uuid);

    S2cMessage response = new S2cMessage();
    response.createAlert = new S2cCreateAlertMessage();
    response.createAlert.uuid = alert.uuid;
    response.createAlert.name = alert.name;
    response.createAlert.description = alert.description;
    response.createAlert.command = alert.command;
    response.createAlert.frequency = alert.frequency;
    response.createAlert.recipientUuids = alert.recipientUuids;
    response.createAlert.tags = alert.tags;
  }

  private void handle(C2sAddTagMessage addTag) {
    // TODO
  }

  private void handle(C2sEditAlertNameMessage editAlertName) {
    String alertUuid = editAlertName.alertUuid;
    String newName = editAlertName.newName;
    Alert alert = AlertStorage.getAlertByUuid(alertUuid).getOrThrow("No such alert: " + alertUuid);
    alert.name = newName;
    AlertStorage.updateAlert(alert);

    S2cMessage response = new S2cMessage();
    response.editAlertName = new S2cEditAlertNameMessage();
    response.editAlertName.alertUuid = alertUuid;
    response.editAlertName.newName = newName;
  }

  private void handle(C2sEditAlertDescriptionMessage editAlertDescription) {
    String alertUuid = editAlertDescription.alertUuid;
    String newDescription = editAlertDescription.newDescription;
    Alert alert = AlertStorage.getAlertByUuid(alertUuid).getOrThrow("No such alert: " + alertUuid);
    alert.description = newDescription;
    AlertStorage.updateAlert(alert);

    S2cMessage response = new S2cMessage();
    response.editAlertDescription = new S2cEditAlertDescriptionMessage();
    response.editAlertDescription.alertUuid = alertUuid;
    response.editAlertDescription.newDescription = newDescription;
  }

  private void handle(C2sEditAlertCommandMessage editAlertCommand) {
    String alertUuid = editAlertCommand.alertUuid;
    String newCommand = editAlertCommand.newCommand;
    Alert alert = AlertStorage.getAlertByUuid(alertUuid).getOrThrow("No such alert: " + alertUuid);
    alert.command = newCommand;
    AlertStorage.updateAlert(alert);

    S2cMessage response = new S2cMessage();
    response.editAlertCommand = new S2cEditAlertCommandMessage();
    response.editAlertCommand.alertUuid = alertUuid;
    response.editAlertCommand.newCommand = newCommand;
    broadcast(response);
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
