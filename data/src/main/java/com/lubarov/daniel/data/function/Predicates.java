package com.lubarov.daniel.data.function;

/**
 * Utilities for working with boolean {@link Function}s.
 */
public final class Predicates {
  private Predicates() {}

  public static <A> Function<A, Boolean> opposite(final Function<? super A, Boolean> predicate) {
    return new Function<A, Boolean>() {
      @Override
      public Boolean apply(A input) {
        return !predicate.apply(input);
      }
    };
  }

  public static <A> Function<A, Boolean> conjunction(
      final Function<? super A, Boolean> predicateA,
      final Function<? super A, Boolean> predicateB) {
    return new Function<A, Boolean>() {
      @Override
      public Boolean apply(A input) {
        return predicateA.apply(input) && predicateB.apply(input);
      }
    };
  }

  public static <A> Function<A, Boolean> disjunction(
      final Function<? super A, Boolean> predicateA,
      final Function<? super A, Boolean> predicateB) {
    return new Function<A, Boolean>() {
      @Override
      public Boolean apply(A input) {
        return predicateA.apply(input) || predicateB.apply(input);
      }
    };
  }

  public static <A> Function<A, Boolean> exclusiveDisjunction(
      final Function<? super A, Boolean> predicateA,
      final Function<? super A, Boolean> predicateB) {
    return new Function<A, Boolean>() {
      @Override
      public Boolean apply(A input) {
        return predicateA.apply(input) ^ predicateB.apply(input);
      }
    };
  }
}
