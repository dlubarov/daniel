package daniel.data.stack;

import daniel.data.source.RandomAccessEnumerator;
import daniel.data.source.Source;
import java.util.ArrayList;
import java.util.Arrays;

public final class DynamicArray<A> extends AbstractMutableStack<A> {
  private final ArrayList<A> proxy;

  private DynamicArray(ArrayList<A> proxy) {
    this.proxy = proxy;
  }

  public static <A> DynamicArray<A> create() {
    return new DynamicArray<A>(new ArrayList<A>());
  }

  public static <A> DynamicArray<A> create(A... values) {
    return new DynamicArray<A>(new ArrayList<A>(Arrays.asList(values)));
  }

  public static <A> DynamicArray<A> copyOf(Iterable<? extends A> iterable) {
    ArrayList<A> proxy = new ArrayList<A>();
    for (A element : iterable)
      proxy.add(element);
    return new DynamicArray<A>(proxy);
  }

  @Override
  public void pushBack(A value) {
    proxy.add(value);
  }

  @Override
  public A popBack() {
    return proxy.remove(getSize() - 1);
  }

  @Override
  public A get(int index) {
    return proxy.get(index);
  }

  public void set(int index, A value) {
    proxy.set(index, value);
  }

  @Override
  public Source<A> getEnumerator() {
    return new RandomAccessEnumerator<A>(this);
  }

  @Override
  public int getSize() {
    return proxy.size();
  }
}
