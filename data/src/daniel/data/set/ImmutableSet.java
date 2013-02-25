package daniel.data.set;

import daniel.data.collection.ImmutableCollection;

/**
 * A {@link Set} whose elements never change.
 */
public interface ImmutableSet<A> extends Set<A>, ImmutableCollection<A> {}
