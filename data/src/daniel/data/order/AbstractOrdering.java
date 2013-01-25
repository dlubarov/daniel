package daniel.data.order;

import java.util.Comparator;

public abstract class AbstractOrdering<A> implements Ordering<A> {
  @Override
  public Ordering<A> reverse() {
    return new ReverseOrdering<A>(this);
  }

  @Override
  public Comparator<A> toComparator() {
    return new OrderingComparator<>(this);
  }
}
