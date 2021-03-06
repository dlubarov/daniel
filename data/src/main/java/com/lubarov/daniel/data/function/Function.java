package com.lubarov.daniel.data.function;

/**
 * A conversion which maps one object to another.
 *
 * @param <A> the input type
 * @param <B> the output type
 */
public interface Function<A, B> {
  B apply(A input);
}
