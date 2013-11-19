package daniel.nagger.cmd;

import daniel.logging.Logger;
import daniel.nagger.Status;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public final class CommandExecutor {
  private static final Logger logger = Logger.forClass(CommandExecutor.class);

  private CommandExecutor() {}

  public static CommandResult execute(String command) {
    try {
      return tryExecute(command);
    } catch (Exception e) {
      logger.warn(e, "Exception while executing command: %s.", command);
      return new CommandResult(Status.UNKNOWN, e.getMessage());
    }
  }

  private static CommandResult tryExecute(String command) throws IOException, InterruptedException {
    logger.info("Executing %s.", command);
    String[] cmdarray = {"bash", "-c", command};
    String[] envp = {};
    Process process = Runtime.getRuntime().exec(cmdarray, envp, null);
    int exitStatus = process.waitFor();
    Status status = Status.fromExitStatus(exitStatus);
    Reader reader = new InputStreamReader(process.getInputStream());
    String output = readAll(reader).trim();
    CommandResult result = new CommandResult(status, output);
    logger.info("Returning %s", result);
    return result;
  }

  private static String readAll(Reader reader) throws IOException {
    char[] cbuf = new char[1024];
    StringBuilder sb = new StringBuilder();
    for (;;) {
      int nread = reader.read(cbuf);
      if (nread == -1)
        return sb.toString();
      sb.append(cbuf, 0, nread);
    }
  }
}
