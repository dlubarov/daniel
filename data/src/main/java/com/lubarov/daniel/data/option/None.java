package com.lubarov.daniel.data.option;

import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.source.EmptySource;
import com.lubarov.daniel.data.source.Source;

/**
 * An {@link Option} which does not contain a value. In other words, it is an empty collection.
 */
final class None<A> extends Option<A> {
  @Override
  public Source<A> getEnumerator() {
    return new EmptySource<>();
  }

  @Override
  public int getSize() {
    return 0;
  }

  @Override
  public <B> Option<B> map(Function<? super A, ? extends B> transformation) {
    return Option.none();
  }

  @Override
  public Option<A> filter(Function<? super A, Boolean> predicate) {
    return this;
  }

  @Override
  public A getOrNull() {
    return null;
  }

  @Override
  public A getOrDefault(A defaultValue) {
    return defaultValue;
  }

  @Override
  public A getOrThrow(RuntimeException e) {
    throw e;
  }

  @Override
  public A getOrThrow(String exceptionMessageFormat, Object... args) {
    throw new RuntimeException(String.format(exceptionMessageFormat, args));
  }
}
