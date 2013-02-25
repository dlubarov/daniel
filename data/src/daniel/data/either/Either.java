package daniel.data.either;

import daniel.data.function.Function;

/**
 * An object which holds either an A or a B, but not both.
 *
 * @param <A> the left type
 * @param <B> the right type
 */
public abstract class Either<A, B> {
  Either() {}

  public static <A, B> Either<A, B> left(A value) {
    return new Left<>(value);
  }

  public static <A, B> Either<A, B> right(B value) {
    return new Right<>(value);
  }

  public abstract <C> Either<C, B> mapLeft(Function<? super A, ? extends C> transformation);

  public abstract <C> Either<A, C> mapRight(Function<? super B, ? extends C> transformation);
}
