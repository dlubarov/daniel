package daniel.nagger.cmd;

import daniel.data.util.ToStringBuilder;
import daniel.nagger.Status;

public class CommandResult {
  public final Status status;
  public final String output;

  public CommandResult(Status status, String output) {
    this.status = status;
    this.output = output;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("status", status)
        .append("output", output)
        .toString();
  }
}
