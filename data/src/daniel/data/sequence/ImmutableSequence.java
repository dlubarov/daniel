package daniel.data.sequence;

import daniel.data.collection.ImmutableCollection;

/**
 * A {@link Sequence} whose elements (and their order) never change.
 */
public interface ImmutableSequence<A> extends Sequence<A>, ImmutableCollection<A> {
}
