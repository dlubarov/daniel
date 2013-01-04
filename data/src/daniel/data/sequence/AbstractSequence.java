package daniel.data.sequence;

import daniel.data.collection.AbstractCollection;
import daniel.data.deque.ArrayDeque;
import daniel.data.deque.MutableDeque;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.order.Ordering;
import daniel.data.source.Source;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
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
    List<A> jcfList = new ArrayList<A>(getSize());
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
  public Sequence<A> sorted(Ordering<? super A> ordering) {
    List<A> jcfList = toJCF();
    Collections.sort(jcfList, ordering.toComparator());
    return ImmutableArray.copyOf(jcfList);
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
    return map(new Function<A, A>() {
      @Override public A apply(A element) {
        return element.equals(value) ? replacement : element;
      }
    });
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
    return reversed().getFirstIndex(value).map(new Function<Integer, Integer>() {
      @Override public Integer apply(Integer index) {
        return getSize() - index - 1;
      }
    });
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
