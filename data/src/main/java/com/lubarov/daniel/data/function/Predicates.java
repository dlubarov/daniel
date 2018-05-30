package com.lubarov.daniel.data.function;

/**
 * Utilities for working with boolean {@link Function}s.
 */
public final class Predicates {
  private Predicates() {}

  public static <A> Function<A, Boolean> opposite(final Function<? super A, Boolean> predicate) {
    return input -> !predicate.apply(input);
  }

  public static <A> Function<A, Boolean> conjunction(
      final Function<? super A, Boolean> predicateA,
      final Function<? super A, Boolean> predicateB) {
    return input -> predicateA.apply(input) && predicateB.apply(input);
  }

  public static <A> Function<A, Boolean> disjunction(
      final Function<? super A, Boolean> predicateA,
      final Function<? super A, Boolean> predicateB) {
    return input -> predicateA.apply(input) || predicateB.apply(input);
  }

  public static <A> Function<A, Boolean> exclusiveDisjunction(
      final Function<? super A, Boolean> predicateA,
      final Function<? super A, Boolean> predicateB) {
    return input -> predicateA.apply(input) ^ predicateB.apply(input);
  }
}
