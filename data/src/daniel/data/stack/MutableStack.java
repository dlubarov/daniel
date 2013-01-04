package daniel.data.stack;

import daniel.data.sequence.Sequence;

public interface MutableStack<A> extends Sequence<A> {
  public void pushBack(A value);

  public A popBack();
}
