package com.lubarov.daniel.data.source;

import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;

/**
 * A stream of objects which may or may not be finite, similar to an {@link java.util.Iterator}.
 */
public interface Source<A> {
  Option<A> tryTake();

  Sequence<A> takeUpTo(int n);

  Sequence<A> takeExactly(int n);

  Sequence<A> takeAll();

  <B> Source<B> map(Function<? super A, ? extends B> transformation);

  Source<A> filter(Function<? super A, Boolean> predicate);
}
