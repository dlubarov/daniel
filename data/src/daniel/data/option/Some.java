package daniel.data.option;

import daniel.data.function.Function;
import daniel.data.source.SingletonSource;
import daniel.data.source.Source;
import daniel.data.util.Check;

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
    return predicate.apply(value) ? this : Option.<A>none();
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
  public A getOrThrow(String exceptionMessage) {
    return value;
  }
}