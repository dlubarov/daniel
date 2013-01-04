package daniel.data.sequence;

import daniel.data.source.RandomAccessEnumerator;
import daniel.data.source.Source;
import daniel.data.stack.DynamicArray;
import daniel.data.util.ArrayUtils;

public final class MutableArray<A> extends AbstractSequence<A> {
  private final A[] proxy;

  private MutableArray(A[] proxy) {
    this.proxy = proxy;
  }

  @SuppressWarnings("unchecked")
  public static <A> MutableArray<A> create() {
    return new MutableArray<A>((A[]) ArrayUtils.EMPTY_ARRAY);
  }

  public static <A> MutableArray<A> create(A... values) {
    return new MutableArray<A>(values.clone());
  }

  @SuppressWarnings("unchecked")
  public static <A> MutableArray<A> createWithNulls(int n) {
    return new MutableArray<A>((A[]) new Object[n]);
  }

  @SuppressWarnings("unchecked")
  public static <A> MutableArray<A> copyOf(Iterable<? extends A> iterable) {
    return new MutableArray<A>((A[]) DynamicArray.copyOf(iterable).toArray());
  }

  @Override
  public A get(int index) {
    return proxy[index];
  }

  public void set(int index, A value) {
    proxy[index] = value;
  }

  @Override
  public Source<A> getEnumerator() {
    return new RandomAccessEnumerator<A>(this);
  }

  @Override
  public int getSize() {
    return proxy.length;
  }
}
