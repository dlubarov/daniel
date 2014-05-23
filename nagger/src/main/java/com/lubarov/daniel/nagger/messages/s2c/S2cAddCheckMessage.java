package com.lubarov.daniel.nagger.messages.s2c;

import com.lubarov.daniel.nagger.Status;

public class S2cAddCheckMessage {
  public String alertUuid;
  public long triggeredAtMillis;
  public int durationMillis;
  public Status status;
  public String details;
}
