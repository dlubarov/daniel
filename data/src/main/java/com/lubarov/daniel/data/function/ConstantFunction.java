package com.lubarov.daniel.data.function;

/**
 * A {@link Function} which ignores its input and always produces the same output.
 */
public final class ConstantFunction<A, B> implements Function<A, B> {
  private final B value;

  public ConstantFunction(B value) {
    this.value = value;
  }

  @Override
  public B apply(A input) {
    return value;
  }
}
