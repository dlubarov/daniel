package daniel.data.set;

import daniel.data.collection.Collection;
import daniel.data.function.Function;

/**
 * A {@link Collection} which contains no duplicate elements.
 */
public interface Set<A> extends Collection<A> {
  @Override
  public Set<A> filter(Function<? super A, Boolean> predicate);

  public ImmutableSet<A> toImmutable();

  // TODO: intersection, union, etc.
  //public Set<A> intersection(Set<?> that);
}
