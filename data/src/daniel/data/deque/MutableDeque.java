package daniel.data.deque;

import daniel.data.queue.MutableQueue;
import daniel.data.stack.MutableStack;

public interface MutableDeque<A> extends MutableStack<A>, MutableQueue<A> {
  public void pushFront(A value);
}
