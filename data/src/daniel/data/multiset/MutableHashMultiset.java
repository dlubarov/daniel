package daniel.data.multiset;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashDictionary;
import daniel.data.source.Source;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public final class MutableHashMultiset<A> extends AbstractMutableMultiset<A> {
  private final MutableHashDictionary<A, Integer> elementCounts;
  private int size;

  private MutableHashMultiset() {
    this.elementCounts = MutableHashDictionary.create();
    this.size = 0;
  }

  public static <A> MutableHashMultiset<A> create() {
    return new MutableHashMultiset<>();
  }

  public static <A> MutableHashMultiset<A> copyOf(Iterable<A> iterable) {
    MutableHashMultiset<A> result = new MutableHashMultiset<>();
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
