package daniel.data.deque;

import daniel.data.source.RandomAccessEnumerator;
import daniel.data.source.Source;
import daniel.data.util.ArrayUtils;

public final class ArrayDeque<A> extends AbstractMutableDeque<A> {
  private static final int MINIMUM_CAPACITY = 4;

  private CircularBuffer<A> buffer;

  private ArrayDeque(int initialCapacity) {
    buffer = new CircularBuffer<A>(Math.max(initialCapacity, MINIMUM_CAPACITY));
  }

  private ArrayDeque() {
    this(0);
  }

  @SuppressWarnings("unchecked")
  public static <A> ArrayDeque<A> create() {
    return create((A[]) ArrayUtils.EMPTY_ARRAY);
  }

  public static <A> ArrayDeque<A> create(A... values) {
    ArrayDeque<A> deque = new ArrayDeque<A>(values.length);
    for (A element : values)
      deque.pushBack(element);
    return deque;
  }

  public static <A> ArrayDeque<A> copyOf(Iterable<? extends A> iterable) {
    ArrayDeque<A> deque = create();
    for (A element : iterable)
      deque.pushBack(element);
    return deque;
  }

  @Override
  public void pushFront(A value) {
    expandIfAtCapacity();
    buffer.pushFront(value);
  }

  @Override
  public A popFront() {
    shrinkIfWayUnderCapacity();
    return buffer.popFront();
  }

  public void set(int index, A value) {
    buffer.set(index, value);
  }

  public void setAll(A value) {
    for (int i = 0; i < getSize(); ++i)
      set(i, value);
  }

  @Override
  public void pushBack(A value) {
    expandIfAtCapacity();
    buffer.pushBack(value);
  }

  @Override
  public A popBack() {
    shrinkIfWayUnderCapacity();
    return buffer.popBack();
  }

  @Override
  public A get(int index) {
    return buffer.get(index);
  }

  @Override
  public int getSize() {
    return buffer.getSize();
  }

  @Override
  public Source<A> getEnumerator() {
    return new RandomAccessEnumerator<A>(this);
  }

  private void expandIfAtCapacity() {
    if (buffer.isAtCapacity())
      changeCapacity(buffer.getCapacity() * 2);
  }

  private void shrinkIfWayUnderCapacity() {
    if (buffer.getCapacity() > 10 && getSize() * 4 < buffer.getCapacity())
      changeCapacity(buffer.getCapacity() / 2);
  }

  private void changeCapacity(int newCapacity) {
    // TODO: Optimize with arraycopy.
    CircularBuffer<A> newBuffer = new CircularBuffer<A>(newCapacity);
    for (int i = 0; i < getSize(); ++i)
      newBuffer.pushBack(buffer.get(i));
    this.buffer = newBuffer;
  }
}
