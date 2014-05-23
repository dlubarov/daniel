package com.lubarov.daniel.data.sequence;

import com.lubarov.daniel.data.collection.AbstractCollection;
import com.lubarov.daniel.data.deque.ArrayDeque;
import com.lubarov.daniel.data.deque.MutableDeque;
import com.lubarov.daniel.data.function.Function;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.source.Source;
import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class AbstractSequence<A> extends AbstractCollection<A> implements Sequence<A> {
  @Override
  public <B> Sequence<B> map(Function<? super A, ? extends B> transformation) {
    MutableStack<B> mappedElements = DynamicArray.create();
    for (A element : this)
      mappedElements.pushBack(transformation.apply(element));
    return mappedElements;
  }

  @Override
  public Sequence<A> filter(Function<? super A, Boolean> predicate) {
    MutableStack<A> filteredElements = DynamicArray.create();
    for (A element : this)
      if (predicate.apply(element))
        filteredElements.pushBack(element);
    return filteredElements;
  }

  @Override
  public ImmutableSequence<A> toImmutable() {
    return ImmutableArray.copyOf(this);
  }

  @Override
  public java.util.List<A> toJCF() {
    List<A> jcfList = new ArrayList<>(getSize());
    for (A element : this)
      jcfList.add(element);
    return jcfList;
  }

  @Override
  public A getFront() {
    return get(0);
  }

  @Override
  public A getBack() {
    return get(getSize() - 1);
  }

  @Override
  public Sequence<A> getTail() {
    MutableDeque<A> deque = ArrayDeque.copyOf(this);
    deque.popFront();
    return deque;
  }

  @Override
  public Sequence<A> getInit() {
    MutableDeque<A> deque = ArrayDeque.copyOf(this);
    deque.popBack();
    return deque;
  }

  @Override
  public Sequence<A> reversed() {
    return SinglyLinkedList.copyOf(this).reversed();
  }

  @Override
  public Sequence<A> shuffled(Random random) {
    List<A> jcfList = toJCF();
    Collections.shuffle(jcfList, random);
    return ImmutableArray.copyOf(jcfList);
  }

  @Override
  public Sequence<A> replaceAll(final A value, final A replacement) {
    return map(element -> element.equals(value) ? replacement : element);
  }

  @Override
  public Option<Integer> getFirstIndex(A value) {
    Source<A> enumerator = getEnumerator();
    for (int i = 0;; ++i) {
      Option<A> element = enumerator.tryTake();
      if (element.isEmpty())
        return Option.none();
      if (element.getOrThrow().equals(value))
        return Option.some(i);
    }
  }

  @Override
  public Option<Integer> getLastIndex(A value) {
    return reversed().getFirstIndex(value).map(index -> getSize() - index - 1);
  }

  @Override
  public String join(String glue) {
    StringBuilder sb = new StringBuilder();
    for (A element : this) {
      if (sb.length() > 0)
        sb.append(glue);
      sb.append(element);
    }
    return sb.toString();
  }
}
