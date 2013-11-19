package daniel.nagger.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Alert {
  public String uuid;
  public String name;
  public String description;
  public String command;
  public String frequency;
  public Set<String> tags = new HashSet<>();
  public Set<String> recipientUuids = new HashSet<>();
  public List<Check> checks = new ArrayList<>();
}
