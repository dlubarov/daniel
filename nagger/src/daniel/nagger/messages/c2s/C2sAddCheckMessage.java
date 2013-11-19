package daniel.nagger.messages.c2s;

import daniel.nagger.Status;

public class C2sAddCheckMessage {
  public String alertUuid;
  public long triggeredAtMillis;
  public int durationMillis;
  public Status status;
  public String details;
}
