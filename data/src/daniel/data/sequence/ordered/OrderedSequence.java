package daniel.data.sequence.ordered;

import daniel.data.order.Ordering;
import daniel.data.sequence.Sequence;

public interface OrderedSequence<A> extends Sequence<A> {
  public Ordering<A> getOrdering();
}
