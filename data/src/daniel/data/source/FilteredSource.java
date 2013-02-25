package daniel.data.source;

import daniel.data.function.Function;
import daniel.data.option.Option;

/**
 * A lazy filter around another {@link Source}.
 */
final class FilteredSource<A> extends AbstractSource<A> {
  private final Source<A> original;
  private final Function<? super A, Boolean> predicate;

  FilteredSource(Source<A> original, Function<? super A, Boolean> predicate) {
    this.original = original;
    this.predicate = predicate;
  }

  @Override
  public Option<A> tryTake() {
    for (;;) {
      Option<A> optNext = original.tryTake();
      if (optNext.isEmpty() || predicate.apply(optNext.getOrThrow()))
        return optNext;
    }
  }
}
