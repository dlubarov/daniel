package daniel.data.set.ordered;

import daniel.data.function.Function;
import daniel.data.sequence.ordered.OrderedSequence;
import daniel.data.set.Set;

public interface OrderedSet<A> extends Set<A>, OrderedSequence<A> {
  @Override
  public OrderedSet<A> filter(Function<? super A, Boolean> predicate);

  @Override
  public ImmutableOrderedSet<A> toImmutable();
}
