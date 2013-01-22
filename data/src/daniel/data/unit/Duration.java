package daniel.data.unit;

public final class Duration {
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
}
