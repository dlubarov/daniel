package daniel.data.order;

import java.util.Comparator;

/**
 * An ordering of objects, similar to {@link Comparator}.
 */
public interface Ordering<A> {
  public Relation compare(A a, A b);

  public Ordering<A> reverse();

  public Comparator<A> toComparator();
}
