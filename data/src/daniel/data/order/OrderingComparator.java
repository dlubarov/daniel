package daniel.data.order;

import java.util.Comparator;

class OrderingComparator<A> implements Comparator<A> {
  private final Ordering<A> ordering;

  OrderingComparator(Ordering<A> ordering) {
    this.ordering = ordering;
  }

  @Override
  public int compare(A a, A b) {
    switch (ordering.compare(a, b)) {
      case LT:
        return -1;
      case EQ:
        return 0;
      case GT:
        return 1;
      default:
        throw new AssertionError();
    }
  }
}
