package daniel.data.source;

import daniel.data.option.Option;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.Sequence;

public final class RecursiveSource<A> extends AbstractSource<A> {
  private final Source<Source<A>> delegateSource;
  private Option<Source<A>> currentDelegate;

  public RecursiveSource(Source<Source<A>> delegates) {
    this.delegateSource = delegates;
    this.currentDelegate = delegateSource.tryTake();
  }

  public RecursiveSource(Sequence<Source<A>> delegates) {
    this(delegates.getEnumerator());
  }

  @SafeVarargs
  public RecursiveSource(Source<A>... delegates) {
    this(ImmutableArray.create(delegates));
  }

  @Override
  public Option<A> tryTake() {
    while (!currentDelegate.isEmpty()) {
      Option<A> next = currentDelegate.getOrThrow().tryTake();
      if (next.isDefined())
        return next;
      currentDelegate = delegateSource.tryTake();
    }
    return Option.none();
  }
}
