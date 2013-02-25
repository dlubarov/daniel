package daniel.data.option;

import daniel.data.collection.AbstractImmutableCollection;
import daniel.data.function.Function;

/**
 * An object which may or may not hold a value. In other words, it is a
 * {@link daniel.data.collection.Collection} which contains at most one element.
 */
public abstract class Option<A> extends AbstractImmutableCollection<A> {
  Option() {}

  @Override
  public abstract <B> Option<B> map(Function<? super A, ? extends B> transformation);

  @Override
  public abstract Option<A> filter(Function<? super A, Boolean> predicate);

  public static <A> Option<A> some(A value) {
    return new Some<>(value);
  }

  public static <A> Option<A> none() {
    return new None<>();
  }

  public static <A> Option<A> fromNullable(A valueOrNull) {
    return valueOrNull != null ? some(valueOrNull) : Option.<A>none();
  }

  public boolean isDefined() {
    return !isEmpty();
  }

  public abstract A getOrNull();

  public abstract A getOrDefault(A defaultValue);

  public abstract A getOrThrow(RuntimeException e);

  public abstract A getOrThrow(String exceptionMessageFormat, Object... args);

  public A getOrThrow() {
    return getOrThrow(new IllegalStateException("No value to get."));
  }
}
