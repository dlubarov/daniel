package com.lubarov.daniel.parsing;

import com.lubarov.daniel.data.function.Function;

public final class ParseResult<A> {
  private final A value;
  private final int rem;

  public ParseResult(A value, int rem) {
    this.value = value;
    this.rem = rem;
  }

  public A getValue() {
    return value;
  }

  public int getRem() {
    return rem;
  }

  public <B> ParseResult<B> map(Function<? super A, ? extends B> transformation) {
    return new ParseResult<>(transformation.apply(value), rem);
  }
}
