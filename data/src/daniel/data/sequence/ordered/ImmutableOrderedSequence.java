package daniel.data.sequence.ordered;

import daniel.data.sequence.ImmutableSequence;

/**
 * An {@link OrderedSequence} whose elements (and their order) never change.
 */
public interface ImmutableOrderedSequence<A> extends OrderedSequence<A>, ImmutableSequence<A> {
}
