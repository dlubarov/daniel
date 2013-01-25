package daniel.data.collection;

import daniel.data.dictionary.Dictionary;
import daniel.data.dictionary.MutableHashTable;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.order.Ordering;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.Sequence;
import daniel.data.source.SourceIterator;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.data.util.ToStringBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractCollection<A> implements Collection<A> {
  @Override
  public Iterator<A> iterator() {
    return new SourceIterator<>(getEnumerator());
  }

  @Override
  public boolean isEmpty() {
    return getSize() == 0;
  }

  @Override
  public int getCount(A value) {
    int count = 0;
    for (A element : this)
      if (element.equals(value))
        ++count;
    return count;
  }

  @Override
  public boolean contains(A value) {
    return getCount(value) > 0;
  }

  @Override
  public <B> Collection<B> map(Function<? super A, ? extends B> transformation) {
    MutableStack<B> mappedElements = DynamicArray.create();
    for (A element : this)
      mappedElements.pushBack(transformation.apply(element));
    return mappedElements;
  }

  @Override
  public Collection<A> filter(Function<? super A, Boolean> predicate) {
    MutableStack<A> filteredElements = DynamicArray.create();
    for (A element : this)
      if (predicate.apply(element))
        filteredElements.pushBack(element);
    return filteredElements;
  }

  @Override
  public <K> Dictionary<K, ? extends Collection<A>> groupBy(Function<? super A, ? extends K> grouper) {
    MutableHashTable<K, MutableStack<A>> groups = MutableHashTable.create();
    for (A element : this) {
      K key = grouper.apply(element);
      if (!groups.containsKey(key))
        groups.put(key, DynamicArray.<A>create());
      groups.getValue(key).pushBack(element);
    }
    return groups;
  }

  @Override
  public Sequence<A> sorted(Ordering<? super A> ordering) {
    List<A> jcfList = new ArrayList<>(toJCF());
    Collections.sort(jcfList, ordering.toComparator());
    return ImmutableArray.copyOf(jcfList);
  }

  @Override
  public Option<A> tryGetOnlyElement() {
    return getSize() == 1
        ? Option.some(getEnumerator().tryTake().getOrThrow())
        : Option.<A>none();
  }

  @Override
  public ImmutableCollection<A> toImmutable() {
    return ImmutableArray.copyOf(this);
  }

  @Override
  public java.util.Collection<A> toJCF() {
    ArrayList<A> arrayList = new ArrayList<>();
    for (A element : this)
      arrayList.add(element);
    return arrayList;
  }

  @Override
  public Object[] toArray() {
    Object[] array = new Object[getSize()];
    int i = 0;
    for (A element : this)
      array[i++] = element;
    return array;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Collection<?> &&
        EqualityTester.areCollectionsEqual(this, (Collection<?>) o);
  }

  @Override
  public int hashCode() {
    int sum = 0;
    for (A element : this)
      sum += element.hashCode();
    return sum;
  }

  @Override
  public String toString() {
    ToStringBuilder toStringBuilder = new ToStringBuilder(this);
    for (A element : this)
      toStringBuilder.append(element);
    return toStringBuilder.toString();
  }
}
