package daniel.nagger;

import daniel.nagger.cmd.CommandExecutor;
import daniel.nagger.cmd.CommandResult;
import daniel.nagger.messages.s2c.S2cAddCheckMessage;
import daniel.nagger.messages.s2c.S2cMessage;
import daniel.nagger.model.Alert;
import daniel.nagger.model.Check;
import daniel.nagger.storage.AlertStorage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class AlertProcessor {
  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10,
      new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
          Thread thread = new Thread(r);
          thread.setName("alert processor");
          thread.setDaemon(true);
          return thread;
        }
  });

  public AlertProcessor() {
    for (Alert alert : AlertStorage.getAllAlerts()) {
      startProcessing(alert.uuid);
    }
  }

  public void startProcessing(final String alertUuid) {
    Runnable task = new Runnable() {
      @Override public void run() {
        Alert alert = AlertStorage.getAlertByUuid(alertUuid).getOrThrow();
        Check check = new Check();
        S2cAddCheckMessage addCheckMessage = new S2cAddCheckMessage();
        addCheckMessage.alertUuid = alertUuid;
        addCheckMessage.triggeredAtMillis = check.triggeredAtMillis = System.currentTimeMillis();
        CommandResult result = CommandExecutor.execute(alert.command);
        addCheckMessage.durationMillis = check.durationMillis =
            (int) (System.currentTimeMillis() - check.triggeredAtMillis);
        addCheckMessage.status = check.status = result.status;
        addCheckMessage.details = check.details = result.output;
        alert.addCheck(check);
        AlertStorage.updateAlert(alert);

        S2cMessage message = new S2cMessage();
        message.addCheck = addCheckMessage;
        NaggerWebSocketHandler.singleton.broadcast(message);

        Frequency frequency = FrequencyParser.parse(alert.frequency);
        executorService.schedule(this, frequency.amount, frequency.unit);
      }
    };
    executorService.execute(task);
  }
}
