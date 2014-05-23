package com.lubarov.daniel.data.source;

import com.lubarov.daniel.data.option.Option;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} which draws its values from a {@link Source}.
 */
public final class SourceIterator<A> implements Iterator<A> {
  private final Source<A> source;
  private Option<A> next;

  public SourceIterator(Source<A> source) {
    this.source = source;
    next = source.tryTake();
  }

  @Override
  public boolean hasNext() {
    return next.isDefined();
  }

  @Override
  public A next() {
    if (!hasNext())
      throw new NoSuchElementException();
    A result = next.getOrThrow();
    next = source.tryTake();
    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
