package com.lubarov.daniel.data.queue;

import com.lubarov.daniel.data.sequence.Sequence;

/**
 * A structure which supports pushing values onto the back and popping values off of the front.
 */
public interface MutableQueue<A> extends Sequence<A> {
  void pushBack(A value);

  A popFront();
}
