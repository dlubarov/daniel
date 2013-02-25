package daniel.data.source;

import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;

/**
 * A stream of objects which may or may not be finite, similar to an {@link java.util.Iterator}.
 */
public interface Source<A> {
  public Option<A> tryTake();

  public Sequence<A> takeUpTo(int n);

  public Sequence<A> takeExactly(int n);

  public Sequence<A> takeAll();

  public <B> Source<B> map(Function<? super A, ? extends B> transformation);

  public Source<A> filter(Function<? super A, Boolean> predicate);
}
