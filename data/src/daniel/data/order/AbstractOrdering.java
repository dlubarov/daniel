package daniel.data.order;

import java.util.Comparator;

public abstract class AbstractOrdering<A> implements Ordering<A> {
  public Comparator<A> toComparator() {
    return new OrderingComparator<A>(this);
  }
}
