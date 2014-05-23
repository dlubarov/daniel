package com.lubarov.daniel.data.bag;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.dictionary.MutableHashDictionary;
import com.lubarov.daniel.data.source.Source;
import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;

public final class MutableHashBag<A> extends AbstractMutableBag<A> {
  private final MutableHashDictionary<A, Integer> elementCounts;
  private int size;

  private MutableHashBag() {
    this.elementCounts = MutableHashDictionary.create();
    this.size = 0;
  }

  public static <A> MutableHashBag<A> create() {
    return new MutableHashBag<>();
  }

  public static <A> MutableHashBag<A> copyOf(Iterable<A> iterable) {
    MutableHashBag<A> result = new MutableHashBag<>();
    for (A element : iterable)
      result.add(element);
    return result;
  }

  @Override
  public Source<A> getEnumerator() {
    // TODO: Write an enumerator which doesn't build a copy.
    MutableStack<A> stack = DynamicArray.create();
    for (KeyValuePair<A, Integer> elementAndCount : elementCounts)
      for (int i = 0; i < elementAndCount.getValue(); ++i)
        stack.pushBack(elementAndCount.getKey());
    return stack.getEnumerator();
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public int getCount(A value) {
    return elementCounts.tryGetValue(value).getOrDefault(0);
  }

  @Override
  public void setCount(A value, int count) {
    if (count < 0)
      throw new IllegalArgumentException("Negative count!");

    size += count - getCount(value);
    if (count == 0)
      elementCounts.tryRemove(value);
    else
      elementCounts.put(value, count);
  }
}
