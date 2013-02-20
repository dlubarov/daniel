package daniel.data.unit;

import daniel.data.order.AbstractOrdering;
import daniel.data.order.Ordering;
import daniel.data.order.Relation;

public final class Duration {
  public static final Ordering<Duration> ASCENDING_ORDERING = new AbstractOrdering<Duration>() {
    @Override public Relation compare(Duration a, Duration b) {
      return a.seconds < b.seconds ? Relation.LT : Relation.GT;
    }
  };

  public static final Ordering<Duration> DESCENDING_ORDERING = ASCENDING_ORDERING.reverse();

  private final double seconds;

  private Duration(double seconds) {
    this.seconds = seconds;
  }

  public static Duration fromNanoseconds(double nanos) {
    return new Duration(nanos / 1e9);
  }

  public static Duration fromMicroseconds(double micros) {
    return new Duration(micros / 1e6);
  }

  public static Duration fromMilliseconds(double millis) {
    return new Duration(millis / 1e3);
  }

  public static Duration fromSeconds(double seconds) {
    return new Duration(seconds);
  }

  public static Duration fromMinutes(double minutes) {
    return new Duration(minutes * 60.0);
  }

  public static Duration fromHours(double hours) {
    return new Duration(hours * 3600.0);
  }

  public static Duration fromDays(double days) {
    return new Duration(days * 86400.0);
  }

  public static Duration fromYears(double years) {
    return new Duration(years * 31536000.0);
  }

  public boolean isLessThan(Duration that) {
    return this.seconds < that.seconds;
  }

  public boolean isGreaterThan(Duration that) {
    return this.seconds > that.seconds;
  }

  public Duration plus(Duration that) {
    return new Duration(seconds + that.seconds);
  }

  public Duration minus(Duration that) {
    return new Duration(seconds - that.seconds);
  }

  public double getNanoseconds() {
    return seconds * 1e9;
  }

  public double getMicroseconds() {
    return seconds * 1e6;
  }

  public double getMilliseconds() {
    return seconds * 1e3;
  }

  public double getSeconds() {
    return seconds;
  }

  public double getMinutes() {
    return seconds / 60.0;
  }

  public double getHours() {
    return seconds / 3600.0;
  }

  public double getDays() {
    return seconds / 86400.0;
  }

  public double getYears() {
    return seconds / 31536000.0;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Duration && seconds == ((Duration) o).seconds;
  }

  @Override
  public int hashCode() {
    return new Double(seconds).hashCode();
  }

  /**
   * Provides a human-friendly representation of this Duration. The exact format is undefined.
   */
  @Override
  public String toString() {
    long baseYears = (long) getYears();
    if (baseYears > 1)
      return baseYears + " years";

    long baseDays = (long) getDays();
    if (baseDays > 1)
      return baseDays + " days";

    long baseHours = (long) getHours();
    if (baseHours > 1)
      return baseHours + " hours";

    long baseMinutes = (long) getMinutes();
    if (baseMinutes > 1)
      return baseMinutes + " minutes";

    long baseSeconds = (long) getSeconds();
    if (baseSeconds > 1)
      return baseSeconds + " seconds";

    return "a moment";
  }
}
