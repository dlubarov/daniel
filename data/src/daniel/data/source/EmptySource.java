package daniel.data.source;

import daniel.data.option.Option;

public final class EmptySource<A> extends AbstractSource<A> {
  @Override
  public Option<A> tryTake() {
    return Option.none();
  }
}
