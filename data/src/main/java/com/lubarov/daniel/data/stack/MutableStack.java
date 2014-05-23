package com.lubarov.daniel.data.stack;

import com.lubarov.daniel.data.sequence.Sequence;

/**
 * A first in, last out structure.
 */
public interface MutableStack<A> extends Sequence<A> {
  public void pushBack(A value);

  public A popBack();
}
