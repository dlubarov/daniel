package com.lubarov.daniel.data.source;

import com.lubarov.daniel.data.option.Option;

/**
 * A {@link Source} which provides no objects.
 */
public final class EmptySource<A> extends AbstractSource<A> {
  @Override
  public Option<A> tryTake() {
    return Option.none();
  }
}
