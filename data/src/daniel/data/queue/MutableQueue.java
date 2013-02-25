package daniel.data.queue;

import daniel.data.sequence.Sequence;

/**
 * A structure which supports pushing values onto the back and popping values off of the front.
 */
public interface MutableQueue<A> extends Sequence<A> {
  public void pushBack(A value);

  public A popFront();
}
