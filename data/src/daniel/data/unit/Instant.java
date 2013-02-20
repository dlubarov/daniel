package daniel.data.unit;

import daniel.data.order.AbstractOrdering;
import daniel.data.order.Ordering;
import daniel.data.order.Relation;
import java.util.Date;

public final class Instant {
  public static final Ordering<Instant> ASCENDING_ORDERING = new AbstractOrdering<Instant>() {
    @Override public Relation compare(Instant a, Instant b) {
      return Duration.ASCENDING_ORDERING.compare(a.sinceEpoch, b.sinceEpoch);
    }
  };

  public static final Ordering<Instant> DESCENDING_ORDERING = ASCENDING_ORDERING.reverse();

  private final Duration sinceEpoch;

  private Instant(Duration sinceEpoch) {
    this.sinceEpoch = sinceEpoch;
  }

  public static Instant now() {
    return new Instant(Duration.fromMilliseconds(System.currentTimeMillis()));
  }

  public static Instant fromDate(Date date) {
    return new Instant(Duration.fromMilliseconds(date.getTime()));
  }

  public static Instant fromDurationSinceEpoch(Duration sinceEpoch) {
    return new Instant(sinceEpoch);
  }

  public boolean isBefore(Instant that) {
    return this.sinceEpoch.isLessThan(that.sinceEpoch);
  }

  public boolean isAfter(Instant that) {
    return this.sinceEpoch.isGreaterThan(that.sinceEpoch);
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

  @Override
  public boolean equals(Object o) {
    return o instanceof Instant && sinceEpoch == ((Instant) o).sinceEpoch;
  }

  @Override
  public int hashCode() {
    return sinceEpoch.hashCode();
  }

  /**
   * Provides a human-friendly representation of this Instant. The exact format is undefined.
   */
  @Override
  public String toString() {
    return toDate().toString();
  }
}
