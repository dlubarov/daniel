package com.lubarov.daniel.data.source;

import com.lubarov.daniel.data.option.Option;

import java.util.Iterator;

/**
 * An {@link Source} which draws its values from a {@link Iterator}.
 */
public final class IteratorSource<A> extends AbstractSource<A> {
  private final Iterator<A> iterator;

  public IteratorSource(Iterator<A> iterator) {
    this.iterator = iterator;
  }

  @Override
  public Option<A> tryTake() {
    return iterator.hasNext() ? Option.some(iterator.next()) : Option.<A>none();
  }
}
