package daniel.data.set;

import daniel.data.source.IteratorSource;
import daniel.data.source.Source;
import java.util.Arrays;

public class ImmutableHashSet<A> extends AbstractImmutableSet<A> {
  private final java.util.HashSet<A> proxy;

  private ImmutableHashSet(java.util.HashSet<A> proxy) {
    this.proxy = proxy;
  }

  public static <A> ImmutableHashSet<A> create() {
    return new ImmutableHashSet<A>(new java.util.HashSet<A>());
  }

  public static <A> ImmutableHashSet<A> create(A... values) {
    return new ImmutableHashSet<A>(new java.util.HashSet<A>(Arrays.asList(values)));
  }

  public static <A> ImmutableHashSet<A> copyOf(Iterable<? extends A> iterable) {
    java.util.HashSet<A> proxy = new java.util.HashSet<A>();
    for (A element : iterable)
      proxy.add(element);
    return new ImmutableHashSet<A>(proxy);
  }

  @Override
  public boolean contains(A value) {
    return proxy.contains(value);
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
