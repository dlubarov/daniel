package daniel.data.order;

final class ReverseOrdering<A> extends AbstractOrdering<A> {
  private final Ordering<A> original;

  ReverseOrdering(Ordering<A> original) {
    this.original = original;
  }

  @Override
  public Relation compare(A a, A b) {
    return original.compare(a, b).getOpposite();
  }
}
