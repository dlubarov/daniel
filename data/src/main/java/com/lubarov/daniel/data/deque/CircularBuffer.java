package com.lubarov.daniel.data.deque;

/**
 * A fixed-capacity buffer of objects which supports efficiently pushing and popping from either
 * end, as well as efficient random access.
 */
final class CircularBuffer<A> {
  private final A[] buffer;
  private int pos = 0, len = 0;

  @SuppressWarnings("unchecked")
  public CircularBuffer(int capacity) {
    buffer = (A[]) new Object[capacity];
  }

  public A get(int index) {
    return buffer[convertIndex(index)];
  }

  public void set(int index, A value) {
    buffer[convertIndex(index)] = value;
  }

  public void pushFront(A value) {
    if (isAtCapacity())
      throw new IllegalStateException("Buffer is full.");
    --pos; wrapPos();
    set(0, value);
  }

  public void pushBack(A value) {
    if (isAtCapacity())
      throw new IllegalStateException("Buffer is full.");
    set(len++, value);
  }

  public A popFront() {
    if (isEmpty())
      throw new IllegalStateException("Buffer is empty.");
    A result = get(0);
    clear(0);
    ++pos; wrapPos();
    return result;
  }

  public A popBack() {
    if (isEmpty())
      throw new IllegalStateException("Buffer is empty.");
    A result = get(--len);
    clear(len);
    return result;
  }

  /**
   * Wrap the position so that it ends up in [0, capacity) in order to prevent overflow/underflow.
   */
  private void wrapPos() {
    pos = (pos % getCapacity() + getCapacity()) % getCapacity();
  }

  private void clear(int index) {
    set(index, null);
  }

  public int getSize() {
    return len;
  }

  public int getCapacity() {
    return buffer.length;
  }

  public boolean isEmpty() {
    return getSize() == 0;
  }

  public boolean isAtCapacity() {
    return getSize() == getCapacity();
  }

  private int convertIndex(int index) {
    return (pos + index) % getCapacity();
  }
}
