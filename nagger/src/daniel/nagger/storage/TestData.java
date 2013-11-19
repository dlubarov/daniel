package daniel.nagger.storage;

import daniel.nagger.Status;
import daniel.nagger.model.Alert;
import daniel.nagger.model.Check;
import daniel.nagger.model.Recipient;
import daniel.web.util.UuidUtils;

public class TestData {
  public static void init() {
    deleteEverything();

    Recipient recipient = createRecipient();

    Alert randomAlert = createRandomAlert(recipient.uuid);
    Alert alwaysOkAlert = createAlwaysOkAlert(recipient.uuid);
    Alert alwaysWarningAlert = createAlwaysWarningAlert(recipient.uuid);
    Alert alwaysCriticalAlert = createAlwaysErrorAlert(recipient.uuid);

    AlertStorage.saveNewAlert(randomAlert);
    AlertStorage.saveNewAlert(alwaysOkAlert);
    AlertStorage.saveNewAlert(alwaysWarningAlert);
    AlertStorage.saveNewAlert(alwaysCriticalAlert);
    RecipientStorage.saveNewRecipient(recipient);
  }

  private static void deleteEverything() {
    AlertStorage.deleteAll();
    RecipientStorage.deleteAll();
  }

  private static Alert createRandomAlert(String recipientUuid) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Random Alert";
    alert.description = "Randomly switches between OK, WARNING and CRITICAL";
    alert.command = "case $(($RANDOM % 3)) in 0) echo 'ok'; exit 0;; 1) echo 'warning'; exit 1;; 2) echo 'critical'; exit 2;; esac";
    alert.frequency = "2 seconds";
    alert.recipientUuids.add(recipientUuid);
    alert.checks.add(createCheck(Status.OK, "All good."));
    return alert;
  }

  private static Alert createAlwaysOkAlert(String recipientUuid) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Auth Decline Rate";
    alert.description = "Checks for abnormally high auth decline rates.";
    alert.command = "exit 0";
    alert.frequency = "5 seconds";
    alert.tags.add("edje");
    alert.tags.add("auth-flow");
    alert.recipientUuids.add(recipientUuid);
    return alert;
  }

  private static Alert createAlwaysWarningAlert(String recipientUuid) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Eero Batch Size";
    alert.description = "Checks if Eero's batch sizes are close to the maximum.";
    alert.command = "echo 'Batch sizes are close the maximum.'; exit 1";
    alert.frequency = "30 seconds";
    alert.recipientUuids.add(recipientUuid);
    alert.checks.add(createCheck(Status.WARNING, "Batch sizes are close to the maximum."));
    alert.tags.add("eero");
    alert.tags.add("settlement");
    return alert;
  }

  private static Alert createAlwaysErrorAlert(String recipientUuid) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "AmEx Batch Size";
    alert.description = "Checks if AmEx's batch sizes are close to the maximum.";
    alert.command = "echo 'Batch sizes are at the max.'; exit 2";
    alert.frequency = "1 minute";
    alert.recipientUuids.add(recipientUuid);
    alert.tags.add("amex");
    alert.tags.add("settlement");
    alert.checks.add(createCheck(Status.OK, "Latest batch size was 1234."));
    return alert;
  }

  private static Recipient createRecipient() {
    Recipient recipient = new Recipient();
    recipient.uuid = UuidUtils.randomAlphanumericUuid();
    recipient.name = "Daniel";
    recipient.command = "sendmail daniel@lubarov.com";
    // TODO: Try this instead.
    //recipient.command = "(echo \"Subject: $ALERT_NAME is $STATUS\"; echo; sendmail daniel@lubarov.com)";
    return recipient;
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
