package daniel.multiweb;

import daniel.logging.Logger;

final class UpdateTask implements Runnable {
  private static final Logger logger = Logger.forClass(UpdateTask.class);

  private static volatile boolean isRunning = false;
  private static volatile boolean shouldRespawn = false;

  private UpdateTask() {}

  public static synchronized void scheduleExecution() {
    if (isRunning)
      shouldRespawn = true;
    else
      spawn();
  }

  private static void spawn() {
    new Thread(new UpdateTask()).start();
  }

  @Override
  public void run() {
    logger.info("Starting update.");

    synchronized (UpdateTask.class) {
      isRunning = true;
    }

    try {
      runWithExceptions();
      synchronized (UpdateTask.class) {
        if (shouldRespawn) {
          shouldRespawn = false;
          spawn();
        }
      }
    } catch (Exception e) {
      logger.error("Error in updater.", e);
    } finally {
      synchronized (UpdateTask.class) {
        isRunning = false;
      }
    }

    logger.info("Finished update.");
  }

  private void runWithExceptions() throws Exception {
    String command = "script/self-update > updater.log";
    new ProcessBuilder("/bin/sh", "-c", command).start().waitFor();
  }
}
