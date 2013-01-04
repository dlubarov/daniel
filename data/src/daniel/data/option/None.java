package daniel.data.option;

import daniel.data.function.Function;
import daniel.data.source.EmptySource;
import daniel.data.source.Source;

final class None<A> extends Option<A> {
  @Override
  public Source<A> getEnumerator() {
    return new EmptySource<A>();
  }

  @Override
  public int getSize() {
    return 0;
  }

  @Override
  public <B> Option<B> map(Function<? super A, ? extends B> transformation) {
    return Option.none();
  }

  @Override
  public Option<A> filter(Function<? super A, Boolean> predicate) {
    return this;
  }

  @Override
  public A getOrNull() {
    return null;
  }

  @Override
  public A getOrDefault(A defaultValue) {
    return defaultValue;
  }

  @Override
  public A getOrThrow(RuntimeException e) {
    throw e;
  }

  @Override
  public A getOrThrow(String exceptionMessage) {
    throw new RuntimeException(exceptionMessage);
  }
}
