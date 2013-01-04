package daniel.data.set;

import daniel.data.source.IteratorSource;
import daniel.data.source.Source;
import java.util.Arrays;

public final class MutableHashSet<A> extends AbstractSet<A> {
  private final java.util.HashSet<A> proxy;

  private MutableHashSet(java.util.HashSet<A> proxy) {
    this.proxy = proxy;
  }

  public static <A> MutableHashSet<A> create() {
    return new MutableHashSet<A>(new java.util.HashSet<A>());
  }

  public static <A> MutableHashSet<A> create(A... values) {
    return new MutableHashSet<A>(new java.util.HashSet<A>(Arrays.asList(values)));
  }

  public static <A> MutableHashSet<A> copyOf(Iterable<? extends A> iterable) {
    java.util.HashSet<A> proxy = new java.util.HashSet<A>();
    for (A element : iterable)
      proxy.add(element);
    return new MutableHashSet<A>(proxy);
  }

  @Override
  public boolean contains(A value) {
    return proxy.contains(value);
  }

  public boolean tryAdd(A value) {
    return proxy.add(value);
  }

  @Override
  public Source<A> getEnumerator() {
    return new IteratorSource<A>(proxy.iterator());
  }

  @Override
  public int getSize() {
    return proxy.size();
  }
}
