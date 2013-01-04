package daniel.data.set.ordered;

import daniel.data.function.Function;
import daniel.data.sequence.ordered.ImmutableOrderedSequence;
import daniel.data.set.ImmutableSet;

public interface ImmutableOrderedSet<A>
    extends OrderedSet<A>, ImmutableSet<A>, ImmutableOrderedSequence<A> {
  @Override
  public OrderedSet<A> filter(Function<? super A, Boolean> predicate);

  @Override
  public ImmutableOrderedSet<A> toImmutable();
}
