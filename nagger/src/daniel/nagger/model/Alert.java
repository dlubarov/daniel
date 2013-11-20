package daniel.nagger.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Alert {
  private static final int MAX_CHECKS_TO_STORE = 20;

  public String uuid;
  public String name;
  public String description;
  public String command;
  public String frequency;
  public Set<String> tags = new HashSet<>();
  public Set<String> recipientUuids = new HashSet<>();
  public LinkedList<Check> checks = new LinkedList<>();

  public void addCheck(Check check) {
    if (checks.size() >= MAX_CHECKS_TO_STORE)
      checks.removeFirst();
    checks.add(check);
  }
}
