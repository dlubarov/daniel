package daniel.nagger.messages.s2c;

import java.util.Set;

public class S2cCreateAlertMessage {
  public String uuid;
  public String name;
  public String description;
  public String command;
  public String frequency;
  public Set<String> recipientUuids;
  public Set<String> tags;
}
