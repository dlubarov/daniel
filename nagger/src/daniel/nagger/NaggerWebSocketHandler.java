package daniel.nagger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daniel.data.set.MutableHashSet;
import daniel.logging.Logger;
import daniel.nagger.messages.c2s.C2sAddRecipientMessage;
import daniel.nagger.messages.c2s.C2sAddTagMessage;
import daniel.nagger.messages.c2s.C2sCreateAlertMessage;
import daniel.nagger.messages.c2s.C2sCreateRecipientMessage;
import daniel.nagger.messages.c2s.C2sEditAlertCommandMessage;
import daniel.nagger.messages.c2s.C2sEditAlertDescriptionMessage;
import daniel.nagger.messages.c2s.C2sEditAlertFrequencyMessage;
import daniel.nagger.messages.c2s.C2sEditAlertNameMessage;
import daniel.nagger.messages.c2s.C2sEditRecipientCommandMessage;
import daniel.nagger.messages.c2s.C2sMessage;
import daniel.nagger.messages.s2c.S2cAddRecipientMessage;
import daniel.nagger.messages.s2c.S2cAddTagMessage;
import daniel.nagger.messages.s2c.S2cCreateAlertMessage;
import daniel.nagger.messages.s2c.S2cCreateRecipientMessage;
import daniel.nagger.messages.s2c.S2cEditAlertCommandMessage;
import daniel.nagger.messages.s2c.S2cEditAlertDescriptionMessage;
import daniel.nagger.messages.s2c.S2cEditAlertFrequencyMessage;
import daniel.nagger.messages.s2c.S2cEditAlertNameMessage;
import daniel.nagger.messages.s2c.S2cEditRecipientCommandMessage;
import daniel.nagger.messages.s2c.S2cJumpToAlertMessage;
import daniel.nagger.messages.s2c.S2cJumpToRecipientMessage;
import daniel.nagger.messages.s2c.S2cMessage;
import daniel.nagger.model.Alert;
import daniel.nagger.model.Recipient;
import daniel.nagger.storage.AlertStorage;
import daniel.nagger.storage.RecipientStorage;
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

  public static final Charset CHARSET = StandardCharsets.UTF_8;

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
      handle(manager, c2sMessage.createAlert);
    if (c2sMessage.createRecipient != null)
      handle(manager, c2sMessage.createRecipient);
    if (c2sMessage.addTag != null)
      handle(c2sMessage.addTag);
    if (c2sMessage.addRecipient != null)
      handle(c2sMessage.addRecipient);
    if (c2sMessage.editAlertName != null)
      handle(c2sMessage.editAlertName);
    if (c2sMessage.editAlertDescription != null)
      handle(c2sMessage.editAlertDescription);
    if (c2sMessage.editAlertCommand != null)
      handle(c2sMessage.editAlertCommand);
    if (c2sMessage.editAlertFrequency != null)
      handle(c2sMessage.editAlertFrequency);
    if (c2sMessage.editRecipientCommand != null)
      handle(c2sMessage.editRecipientCommand);
  }

  private void handle(WebSocketManager client, C2sCreateAlertMessage createAlert) {
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

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.createAlert = new S2cCreateAlertMessage();
    generalUpdate.createAlert.uuid = alert.uuid;
    generalUpdate.createAlert.name = alert.name;
    generalUpdate.createAlert.description = alert.description;
    generalUpdate.createAlert.command = alert.command;
    generalUpdate.createAlert.frequency = alert.frequency;
    generalUpdate.createAlert.recipientUuids = alert.recipientUuids;
    generalUpdate.createAlert.tags = alert.tags;
    broadcast(generalUpdate);

    S2cMessage responseToCreator = new S2cMessage();
    responseToCreator.jumpToAlert = new S2cJumpToAlertMessage();
    responseToCreator.jumpToAlert.alertUuid = alert.uuid;
    send(client, responseToCreator);
  }

  private void handle(WebSocketManager client, C2sCreateRecipientMessage createRecipient) {
    Recipient recipient = new Recipient();
    recipient.uuid = UuidUtils.randomAlphanumericUuid();
    recipient.name = createRecipient.name;
    recipient.command = createRecipient.command;
    RecipientStorage.saveNewRecipient(recipient);

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.createRecipient = new S2cCreateRecipientMessage();
    generalUpdate.createRecipient.uuid = recipient.uuid;
    generalUpdate.createRecipient.name = recipient.name;
    generalUpdate.createRecipient.command = recipient.command;
    broadcast(generalUpdate);

    S2cMessage responseToCreator = new S2cMessage();
    responseToCreator.jumpToRecipient = new S2cJumpToRecipientMessage();
    responseToCreator.jumpToRecipient.recipientUuid = recipient.uuid;
    send(client, responseToCreator);
  }

  private void handle(C2sAddTagMessage addTag) {
    Alert alert = AlertStorage.getAlertByUuid(addTag.alertUuid)
        .getOrThrow("No such alert: " + addTag.alertUuid);
    if (!alert.tags.add(addTag.tag))
      return;
    AlertStorage.updateAlert(alert);

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.addTag = new S2cAddTagMessage();
    generalUpdate.addTag.alertUuid = addTag.alertUuid;
    generalUpdate.addTag.tag = addTag.tag;
    broadcast(generalUpdate);
  }

  private void handle(C2sAddRecipientMessage addRecipient) {
    Alert alert = AlertStorage.getAlertByUuid(addRecipient.alertUuid)
        .getOrThrow("No such alert: " + addRecipient.alertUuid);
    if (!alert.recipientUuids.add(addRecipient.recipientUuid))
      return;
    AlertStorage.updateAlert(alert);

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.addRecipient = new S2cAddRecipientMessage();
    generalUpdate.addRecipient.alertUuid = addRecipient.alertUuid;
    generalUpdate.addRecipient.recipientUuid = addRecipient.recipientUuid;
    broadcast(generalUpdate);
  }

  private void handle(C2sEditAlertNameMessage editAlertName) {
    Alert alert = AlertStorage.getAlertByUuid(editAlertName.alertUuid)
        .getOrThrow("No such alert: " + editAlertName.alertUuid);
    alert.name = editAlertName.newName;
    AlertStorage.updateAlert(alert);

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.editAlertName = new S2cEditAlertNameMessage();
    generalUpdate.editAlertName.alertUuid = editAlertName.alertUuid;
    generalUpdate.editAlertName.newName = editAlertName.newName;
    broadcast(generalUpdate);
  }

  private void handle(C2sEditAlertDescriptionMessage editAlertDescription) {
    Alert alert = AlertStorage.getAlertByUuid(editAlertDescription.alertUuid)
        .getOrThrow("No such alert: " + editAlertDescription.alertUuid);
    alert.description = editAlertDescription.newDescription;
    AlertStorage.updateAlert(alert);

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.editAlertDescription = new S2cEditAlertDescriptionMessage();
    generalUpdate.editAlertDescription.alertUuid = editAlertDescription.alertUuid;
    generalUpdate.editAlertDescription.newDescription = editAlertDescription.newDescription;
    broadcast(generalUpdate);
  }

  private void handle(C2sEditAlertCommandMessage editAlertCommand) {
    Alert alert = AlertStorage.getAlertByUuid(editAlertCommand.alertUuid)
        .getOrThrow("No such alert: " + editAlertCommand.alertUuid);
    alert.command = editAlertCommand.newCommand;
    AlertStorage.updateAlert(alert);

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.editAlertCommand = new S2cEditAlertCommandMessage();
    generalUpdate.editAlertCommand.alertUuid = editAlertCommand.alertUuid;
    generalUpdate.editAlertCommand.newCommand = editAlertCommand.newCommand;
    broadcast(generalUpdate);
  }

  private void handle(C2sEditAlertFrequencyMessage editAlertFrequency) {
    Alert alert = AlertStorage.getAlertByUuid(editAlertFrequency.alertUuid)
        .getOrThrow("No such alert: " + editAlertFrequency.alertUuid);
    alert.frequency = editAlertFrequency.newFrequency;
    AlertStorage.updateAlert(alert);

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.editAlertFrequency = new S2cEditAlertFrequencyMessage();
    generalUpdate.editAlertFrequency.alertUuid = editAlertFrequency.alertUuid;
    generalUpdate.editAlertFrequency.newFrequency = editAlertFrequency.newFrequency;
    broadcast(generalUpdate);
  }

  private void handle(C2sEditRecipientCommandMessage editRecipientCommand) {
    Recipient recipient = RecipientStorage.getRecipientByUuid(editRecipientCommand.recipientUuid)
        .getOrThrow("No such recipient: %s.", editRecipientCommand.recipientUuid);
    recipient.command = editRecipientCommand.newCommand;
    RecipientStorage.updateRecipient(recipient);

    S2cMessage generalUpdate = new S2cMessage();
    generalUpdate.editRecipientCommand = new S2cEditRecipientCommandMessage();
    generalUpdate.editRecipientCommand.recipientUuid = editRecipientCommand.recipientUuid;
    generalUpdate.editRecipientCommand.newCommand = editRecipientCommand.newCommand;
    broadcast(generalUpdate);
  }

  public void broadcast(S2cMessage s2cMessage) {
    for (WebSocketManager client : activeClients)
      send(client, s2cMessage);
  }

  private void send(WebSocketManager client, S2cMessage s2cMessage) {
    client.send(new WebSocketFrame.Builder()
        .setFinalFragment(true)
        .setOpcode(WebSocketOpcode.TEXT_FRAME)
        .setPayload(gson.toJson(s2cMessage).getBytes(CHARSET))
        .build());
  }
}
