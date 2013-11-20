package daniel.nagger.messages.c2s;

import java.util.Set;

public class C2sCreateAlertMessage {
  public String name;
  public String description;
  public String command;
  public String frequency;
  public Set<String> tags;
  public Set<String> recipientUuids;
}
