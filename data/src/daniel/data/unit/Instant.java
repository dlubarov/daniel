package daniel.data.unit;

import java.util.Date;

public final class Instant {
  private final Duration sinceEpoch;

  private Instant(Duration sinceEpoch) {
    this.sinceEpoch = sinceEpoch;
  }

  public static Instant now() {
    return new Instant(Duration.fromMilliseconds(System.currentTimeMillis()));
  }

  public static Instant fromDurationSinceEpoch(Duration sinceEpoch) {
    return new Instant(sinceEpoch);
  }

  public Instant plus(Duration duration) {
    return new Instant(sinceEpoch.plus(duration));
  }

  public Instant minus(Duration duration) {
    return new Instant(sinceEpoch.minus(duration));
  }

  public Date toDate() {
    return new Date((long) sinceEpoch.getMilliseconds());
  }
}
