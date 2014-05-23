package com.lubarov.daniel.data.sequence;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.source.AbstractSource;
import com.lubarov.daniel.data.source.Source;

public abstract class SinglyLinkedList<A> extends AbstractImmutableSequence<A> {
  SinglyLinkedList() {}

  public static <A> SinglyLinkedList<A> create() {
    return new EmptySLL<>();
  }

  @SafeVarargs
  public static <A> SinglyLinkedList<A> create(A... values) {
    SinglyLinkedList<A> backwardsList = new EmptySLL<>();
    for (A element : values)
      backwardsList = new NonEmptySLL<>(element, backwardsList);
    return backwardsList.reversed();
  }

  public static <A> SinglyLinkedList<A> copyOf(Iterable<? extends A> iterable) {
    SinglyLinkedList<A> backwardsList = new EmptySLL<>();
    for (A element : iterable)
      backwardsList = new NonEmptySLL<>(element, backwardsList);
    return backwardsList.reversed();
  }

  @Override
  public Source<A> getEnumerator() {
    return new SLLEnumerator<>(this);
  }

  @Override
  public A get(int index) {
    if (index < 0)
      throw new IndexOutOfBoundsException("Negative index.");
    if (index >= getSize())
      throw new IndexOutOfBoundsException("Index too large.");

    Source<A> enumerator = getEnumerator();
    enumerator.takeExactly(index);
    return enumerator.tryTake().getOrThrow();
  }

  @Override
  public abstract SinglyLinkedList<A> getTail();

  @Override
  public SinglyLinkedList<A> reversed() {
    SinglyLinkedList<A> reversedList = new EmptySLL<>();
    for (A element : this)
      reversedList = new NonEmptySLL<>(element, reversedList);
    return reversedList;
  }

  public SinglyLinkedList<A> plusFront(A value) {
    return new NonEmptySLL<>(value, this);
  }

  private static final class EmptySLL<A> extends SinglyLinkedList<A> {
    @Override
    public int getSize() {
      return 0;
    }

    @Override
    public SinglyLinkedList<A> getTail() {
      throw new IllegalStateException("getRest called on empty list.");
    }
  }

  private static final class NonEmptySLL<A> extends SinglyLinkedList<A> {
    private final A first;
    private final SinglyLinkedList<A> rest;
    private final int size;

    private NonEmptySLL(A first, SinglyLinkedList<A> rest) {
      this.first = first;
      this.rest = rest;
      this.size = rest.getSize() + 1;
    }

    @Override
    public int getSize() {
      return size;
    }

    @Override
    public A getFront() {
      return first;
    }

    @Override
    public SinglyLinkedList<A> getTail() {
      return rest;
    }
  }

  private static final class SLLEnumerator<A> extends AbstractSource<A> {
    private SinglyLinkedList<A> sll;

    private SLLEnumerator(SinglyLinkedList<A> sll) {
      this.sll = sll;
    }

    @Override
    public Option<A> tryTake() {
      if (sll.isEmpty())
        return Option.none();
      A head = sll.getFront();
      sll = sll.getTail();
      return Option.some(head);
    }
  }
}
