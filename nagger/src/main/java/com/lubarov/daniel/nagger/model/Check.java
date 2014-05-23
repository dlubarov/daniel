package com.lubarov.daniel.nagger.model;

import com.lubarov.daniel.nagger.Status;

public class Check {
  public long triggeredAtMillis;
  public int durationMillis;
  public Status status;
  public String details;
}
