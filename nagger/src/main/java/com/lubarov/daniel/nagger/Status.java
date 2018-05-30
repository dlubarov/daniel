package com.lubarov.daniel.nagger;

public enum Status {
  OK(0), WARNING(1), CRITICAL(2), UNKNOWN(3);

  private final int exitStatus;

  public static Status fromExitStatus(int exitStatus) {
    for (Status status : Status.values())
      if (status.exitStatus == exitStatus)
        return status;
    throw new IllegalArgumentException("Bad exit status: " + exitStatus);
  }

  Status(int exitStatus) {
    this.exitStatus = exitStatus;
  }

  private int getExitStatus() {
    return exitStatus;
  }
}
