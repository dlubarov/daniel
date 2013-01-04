package daniel.data.source;

import daniel.data.option.Option;
import java.util.Iterator;

public class IteratorSource<A> extends AbstractSource<A> {
  private final Iterator<A> iterator;

  public IteratorSource(Iterator<A> iterator) {
    this.iterator = iterator;
  }

  @Override
  public Option<A> tryTake() {
    return iterator.hasNext() ? Option.some(iterator.next()) : Option.<A>none();
  }
}
