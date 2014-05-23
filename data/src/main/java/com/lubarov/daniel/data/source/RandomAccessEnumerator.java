package com.lubarov.daniel.data.source;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;

/**
 * A {@link Source} which enumerates a {@link Sequence}'s elements by repeatedly requesting the
 * element at a particular index.
 */
public final class RandomAccessEnumerator<A> extends AbstractSource<A> {
  private final Sequence<A> sequence;
  private int index = 0;

  public RandomAccessEnumerator(Sequence<A> sequence) {
    this.sequence = sequence;
  }

  @Override
  public Option<A> tryTake() {
    if (index >= sequence.getSize())
      return Option.none();
    return Option.some(sequence.get(index++));
  }
}
