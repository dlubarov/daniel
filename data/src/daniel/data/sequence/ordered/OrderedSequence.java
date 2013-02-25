package daniel.data.sequence.ordered;

import daniel.data.order.Ordering;
import daniel.data.sequence.Sequence;

/**
 * A {@link Sequence} whose elements are always ordered according to some {@link Ordering}.
 */
public interface OrderedSequence<A> extends Sequence<A> {
  public Ordering<A> getOrdering();
}
