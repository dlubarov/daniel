package com.lubarov.daniel.data.sequence;

import com.lubarov.daniel.data.collection.ImmutableCollection;

/**
 * A {@link Sequence} whose elements (and their order) never change.
 */
public interface ImmutableSequence<A> extends Sequence<A>, ImmutableCollection<A> {
}
