package daniel.nagger;

import java.util.concurrent.TimeUnit;

public class Frequency {
  public final int amount;
  public final TimeUnit unit;

  public Frequency(int amount, TimeUnit unit) {
    this.amount = amount;
    this.unit = unit;
  }
}
