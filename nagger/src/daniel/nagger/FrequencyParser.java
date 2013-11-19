package daniel.nagger;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public final class FrequencyParser {
  private FrequencyParser() {}

  public static Frequency parse(String frequencyDesc) {
    Scanner sc = new Scanner(frequencyDesc);
    int amount = sc.nextInt();
    String rawUnit = sc.next();
    if (!rawUnit.endsWith("s"))
      rawUnit += "s";
    TimeUnit unit = TimeUnit.valueOf(rawUnit.toUpperCase());
    return new Frequency(amount, unit);
  }
}
