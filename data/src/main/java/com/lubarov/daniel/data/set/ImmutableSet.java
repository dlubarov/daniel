package com.lubarov.daniel.data.set;

import com.lubarov.daniel.data.collection.ImmutableCollection;

/**
 * A {@link Set} whose elements never change.
 */
public interface ImmutableSet<A> extends Set<A>, ImmutableCollection<A> {}
