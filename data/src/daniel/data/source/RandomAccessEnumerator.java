package daniel.data.source;

import daniel.data.option.Option;
import daniel.data.sequence.Sequence;

public class RandomAccessEnumerator<A> extends AbstractSource<A> {
  private final Sequence<A> sequence;
  private int index = 0;

  public RandomAccessEnumerator(Sequence<A> sequence) {
    this.sequence = sequence;
  }

  @Override
  public Option<A> tryTake() {
    if (index >= sequence.getSize())
      return Option.none();
    return Option.some(sequence.get(index++));
  }
}
