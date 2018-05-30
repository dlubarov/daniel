package com.lubarov.daniel.data.option;

import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.source.SingletonSource;
import com.lubarov.daniel.data.source.Source;
import com.lubarov.daniel.data.util.Check;

/**
 * An {@link Option} which contains a single value.
 */
final class Some<A> extends Option<A> {
  private final A value;

  public Some(A value) {
    this.value = Check.notNull(value, "Some constructor received null value.");
  }

  @Override
  public Source<A> getEnumerator() {
    return new SingletonSource<>(value);
  }

  @Override
  public int getSize() {
    return 1;
  }

  @Override
  public <B> Option<B> map(Function<? super A, ? extends B> transformation) {
    return new Some<>(transformation.apply(value));
  }

  @Override
  public Option<A> filter(Function<? super A, Boolean> predicate) {
    return predicate.apply(value) ? this : Option.none();
  }

  @Override
  public A getOrNull() {
    return value;
  }

  @Override
  public A getOrDefault(A defaultValue) {
    return value;
  }

  @Override
  public A getOrThrow(RuntimeException e) {
    return value;
  }

  @Override
  public A getOrThrow(String exceptionMessageFormat, Object... args) {
    return value;
  }
}
