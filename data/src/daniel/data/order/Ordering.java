package daniel.data.order;

import java.util.Comparator;

public interface Ordering<A> {
  public Relation compare(A a, A b);

  public Comparator<A> toComparator();
}
