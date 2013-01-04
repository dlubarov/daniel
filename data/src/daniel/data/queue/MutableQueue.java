package daniel.data.queue;

import daniel.data.sequence.Sequence;

public interface MutableQueue<A> extends Sequence<A> {
  public void pushBack(A value);

  public A popFront();
}
