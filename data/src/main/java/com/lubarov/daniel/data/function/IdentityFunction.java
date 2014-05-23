package com.lubarov.daniel.data.function;

/**
 * A {@link Function} which always outputs its input.
 *
 * @param <A> both the input type and the output type
 */
public final class IdentityFunction<A> implements Function<A, A> {
  @Override
  public A apply(A input) {
    return input;
  }
}
