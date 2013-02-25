package daniel.data.source;

import daniel.data.function.Function;
import daniel.data.option.Option;

/**
 * A lazy transformation around another {@link Source}.
 */
final class MappedSource<A, B> extends AbstractSource<B> {
  private final Source<A> original;
  private final Function<? super A, ? extends B> transformation;

  MappedSource(Source<A> original, Function<? super A, ? extends B> transformation) {
    this.original = original;
    this.transformation = transformation;
  }

  @Override
  public Option<B> tryTake() {
    return original.tryTake().map(transformation);
  }
}
