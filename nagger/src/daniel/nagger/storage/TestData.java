package daniel.nagger.storage;

import daniel.nagger.Status;
import daniel.nagger.model.Alert;
import daniel.nagger.model.Check;
import daniel.nagger.model.Recipient;
import daniel.web.util.UuidUtils;

public class TestData {
  private static final String RECIPIENT_UUID = UuidUtils.randomAlphanumericUuid();

  public static void init() {
    AlertStorage.deleteAll();
    RecipientStorage.deleteAll();

    createRecipient();

    createGatewayAlerts("Gateway X");
    createGatewayAlerts("Gateway Y");
    createGatewayAlerts("Gateway Z");
    createRandomAlert();
  }

  private static void createGatewayAlerts(String gatewayName) {
    createLatencyAlert(gatewayName);
    createDeclineRateAlert(gatewayName);
    createBatchSizeAlert(gatewayName);
  }

  private static void createLatencyAlert(String gatewayName) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = gatewayName + " Auth Decline Rate";
    alert.description = "Checks for an abnormally high auth decline rate.";
    alert.command = "exit 0";
    alert.frequency = "5 seconds";
    alert.tags.add("authorizations");
    alert.tags.add("latency");
    alert.tags.add(gatewayName);
    alert.recipientUuids.add(RECIPIENT_UUID);
    AlertStorage.saveNewAlert(alert);
  }

  private static void createDeclineRateAlert(String gatewayName) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = gatewayName + " Auth Decline Rate";
    alert.description = "Checks for an abnormally high auth decline rate.";
    alert.command = "exit 0";
    alert.frequency = "5 seconds";
    alert.tags.add("decline rate");
    alert.tags.add(gatewayName);
    alert.recipientUuids.add(RECIPIENT_UUID);
    AlertStorage.saveNewAlert(alert);
  }

  private static void createBatchSizeAlert(String gatewayName) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = gatewayName + " Batch Size";
    alert.description = "Checks if " + gatewayName + "'s batch sizes are close to the maximum.";
    alert.command = "echo 'Batch sizes are close the maximum.'; exit 1";
    alert.frequency = "30 seconds";
    alert.recipientUuids.add(RECIPIENT_UUID);
    alert.addCheck(createCheck(Status.WARNING, "Batch sizes are close to the maximum."));
    alert.tags.add("settlement");
    alert.tags.add("batch size");
    alert.tags.add(gatewayName);
    AlertStorage.saveNewAlert(alert);
  }

  private static void createRandomAlert() {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Random Alert";
    alert.description = "Randomly switches between OK, WARNING and CRITICAL.";
    alert.command = "case $(($RANDOM % 3)) in 0) echo 'Randomly decided to return OK.'; exit 0;; 1) echo 'Randomly decided to return WARNING.'; exit 1;; 2) echo 'Randomly decided to return CRITICAL.'; exit 2;; esac";
    alert.frequency = "2 seconds";
    alert.recipientUuids.add(RECIPIENT_UUID);
    alert.addCheck(createCheck(Status.OK, "All good."));
    AlertStorage.saveNewAlert(alert);
  }

  private static void createRecipient() {
    Recipient recipient = new Recipient();
    recipient.uuid = RECIPIENT_UUID;
    recipient.name = "Daniel";
    recipient.command = "sendmail daniel@lubarov.com";
    // TODO: Try this instead.
    //recipient.command = "(echo \"Subject: $ALERT_NAME is $STATUS\"; echo; sendmail daniel@lubarov.com)";
    RecipientStorage.saveNewRecipient(recipient);
  }

  private static Check createCheck(Status status, String details) {
    Check check = new Check();
    check.triggeredAtMillis = System.currentTimeMillis();
    check.durationMillis = 10;
    check.status = status;
    check.details = details;
    return check;
  }
}
