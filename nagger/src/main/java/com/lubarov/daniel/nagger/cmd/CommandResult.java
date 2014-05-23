package com.lubarov.daniel.nagger.cmd;

import com.lubarov.daniel.data.util.ToStringBuilder;
import com.lubarov.daniel.nagger.Status;

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
