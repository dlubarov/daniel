package daniel.data.sequence;

import daniel.data.source.RandomAccessEnumerator;
import daniel.data.source.Source;
import daniel.data.stack.DynamicArray;
import daniel.data.util.ArrayUtils;

public final class ImmutableArray<A> extends AbstractImmutableSequence<A> {
  private final A[] proxy;

  private ImmutableArray(A[] proxy) {
    this.proxy = proxy;
  }

  @SuppressWarnings("unchecked")
  public static <A> ImmutableArray<A> create() {
    return new ImmutableArray<>((A[]) ArrayUtils.EMPTY_ARRAY);
  }

  @SafeVarargs public static <A> ImmutableArray<A> create(A... values) {
    return new ImmutableArray<>(values.clone());
  }

  @SuppressWarnings("unchecked")
  public static <A> ImmutableArray<A> copyOf(Iterable<? extends A> iterable) {
    return new ImmutableArray<>((A[]) DynamicArray.copyOf(iterable).toArray());
  }

  @Override
  public A get(int index) {
    return proxy[index];
  }

  @Override
  public Source<A> getEnumerator() {
    return new RandomAccessEnumerator<>(this);
  }

  @Override
  public int getSize() {
    return proxy.length;
  }
}
