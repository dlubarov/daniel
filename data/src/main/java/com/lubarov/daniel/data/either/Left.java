package com.lubarov.daniel.data.either;

import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.util.ToStringBuilder;

final class Left<A, B> extends Either<A, B> {
  private final A value;

  Left(A value) {
    this.value = value;
  }

  @Override
  public <C> Either<C, B> mapLeft(Function<? super A, ? extends C> transformation) {
    return new Left<>(transformation.apply(value));
  }

  @Override
  public <C> Either<A, C> mapRight(Function<? super B, ? extends C> transformation) {
    return new Left<>(value);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append(value)
        .toString();
  }
}
