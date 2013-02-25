package daniel.data.deque;

import daniel.data.queue.MutableQueue;
import daniel.data.stack.MutableStack;

/**
 * A structure which supports pushing and popping elements on both the front and the back.
 */
public interface MutableDeque<A> extends MutableStack<A>, MutableQueue<A> {
  public void pushFront(A value);
}
