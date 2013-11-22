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

    createHostAlerts("A");
    createHostAlerts("B");
    createHostAlerts("C");
    createHostAlerts("D");
    createHostAlerts("E");
    createHostAlerts("F");

    createGatewayAlerts("X");
    createGatewayAlerts("Y");
    createGatewayAlerts("Z");
  }

  private static void createHostAlerts(String host) {
    createHostCpuAlert(host);
    createHostMemoryAlert(host);
    createHostDiskAlert(host);
  }

  private static void createHostCpuAlert(String host) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Host " + host + " CPU Utilization";
    alert.description = "Checks for an abnormally high CPU utilization.";
    alert.command = randomCommand(
        "CPU is at 5%.",
        "CPU is at 65%.",
        "CPU is at 100%.");
    alert.frequency = "3 seconds";
    alert.tags.add("cpu");
    alert.tags.add("host " + host);
    alert.recipientUuids.add(RECIPIENT_UUID);
    AlertStorage.saveNewAlert(alert);
  }

  private static void createHostMemoryAlert(String host) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Host " + host + " Memory Utilization";
    alert.description = "Checks for an abnormally high memory utilization.";
    alert.command = randomCommand(
        "memory is at 30%.",
        "memory is at 80%.",
        "memory is at 100%.");
    alert.frequency = "3 seconds";
    alert.tags.add("memory");
    alert.tags.add("host " + host);
    alert.recipientUuids.add(RECIPIENT_UUID);
    AlertStorage.saveNewAlert(alert);
  }

  private static void createHostDiskAlert(String host) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Host " + host + " Disk Utilization";
    alert.description = "Checks for an abnormally high disk utilization.";
    alert.command = randomCommand(
        "disk utilization at 45%.",
        "disk utilization at 85%.",
        "disk utilization at 95%.");
    alert.frequency = "3 seconds";
    alert.tags.add("disk");
    alert.tags.add("host " + host);
    alert.recipientUuids.add(RECIPIENT_UUID);
    AlertStorage.saveNewAlert(alert);
  }

  private static void createGatewayAlerts(String gatewayName) {
    createLatencyAlert(gatewayName);
    createDeclineRateAlert(gatewayName);
    createBatchSizeAlert(gatewayName);
  }

  private static void createLatencyAlert(String gatewayName) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Gateway " + gatewayName + " Auth Latency";
    alert.description = "Checks for an abnormally high auth latency.";
    alert.command = randomCommand(
        "Average latency is 600ms.",
        "Average latency is 900ms.",
        "Average latency is 1600ms.");
    alert.frequency = "3 seconds";
    alert.tags.add("authorizations");
    alert.tags.add("latency");
    alert.tags.add("gateway " + gatewayName);
    alert.recipientUuids.add(RECIPIENT_UUID);
    AlertStorage.saveNewAlert(alert);
  }

  private static void createDeclineRateAlert(String gatewayName) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Gateway " + gatewayName + " Auth Decline Rate";
    alert.description = "Checks for an abnormally high auth decline rate.";
    alert.command = randomCommand(
        "Decline rate is 100%.",
        "Decline rate is 90%.",
        "Decline rate is 70%.");
    alert.frequency = "3 seconds";
    alert.tags.add("authorizations");
    alert.tags.add("decline rate");
    alert.tags.add("gateway " + gatewayName);
    alert.recipientUuids.add(RECIPIENT_UUID);
    AlertStorage.saveNewAlert(alert);
  }

  private static void createBatchSizeAlert(String gatewayName) {
    Alert alert = new Alert();
    alert.uuid = UuidUtils.randomAlphanumericUuid();
    alert.name = "Gateway " + gatewayName + " Batch Size";
    alert.description = "Checks if " + gatewayName + "'s batch sizes are close to the maximum.";
    alert.command = randomCommand(
        "Last batch had 60k records.",
        "Last batch had 140k records.",
        "Last batch had 150k records.");
    alert.frequency = "3 seconds";
    alert.recipientUuids.add(RECIPIENT_UUID);
    alert.addCheck(createCheck(Status.WARNING, "Batch sizes are close to the maximum."));
    alert.tags.add("settlement");
    alert.tags.add("batch size");
    alert.tags.add("gateway " + gatewayName);
    AlertStorage.saveNewAlert(alert);
  }

  private static String randomCommand(String ok, String warning, String critical) {
    return new StringBuilder()
        .append("case $(($RANDOM % 3)) in\n")
        .append("  0) echo '").append(ok).append("'; exit 0;;\n")
        .append("  1) echo '").append(warning).append("'; exit 1;;\n")
        .append("  2) echo '").append(critical).append("'; exit 2;;\n")
        .append("esac").toString();
  }

  private static void createRecipient() {
    Recipient recipient = new Recipient();
    recipient.uuid = RECIPIENT_UUID;
    recipient.name = "Daniel";
    recipient.command = "sendmail daniel@lubarov.com";
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
