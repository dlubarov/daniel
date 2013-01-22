package daniel.data.source;

import daniel.data.option.Option;

public final class SingletonSource<A> extends AbstractSource<A> {
  private Option<A> value;

  public SingletonSource(A value) {
    this.value = Option.some(value);
  }

  @Override
  public Option<A> tryTake() {
    Option<A> result = value;
    value = Option.none();
    return result;
  }
}
