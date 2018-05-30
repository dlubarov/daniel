package com.lubarov.daniel.data.deque;

import com.lubarov.daniel.data.queue.MutableQueue;
import com.lubarov.daniel.data.stack.MutableStack;

/**
 * A structure which supports pushing and popping elements on both the front and the back.
 */
public interface MutableDeque<A> extends MutableStack<A>, MutableQueue<A> {
  void pushFront(A value);
}
