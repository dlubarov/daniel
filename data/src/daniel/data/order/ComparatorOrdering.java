package daniel.data.order;

import java.util.Comparator;

public class ComparatorOrdering<A> extends AbstractOrdering<A> {
  private final Comparator<A> comparator;

  public ComparatorOrdering(Comparator<A> comparator) {
    this.comparator = comparator;
  }

  @Override
  public Relation compare(A a, A b) {
    int result = comparator.compare(a, b);
    if (result < 0)
      return Relation.LT;
    if (result > 0)
      return Relation.GT;
    return Relation.EQ;
  }
}
