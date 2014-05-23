package com.lubarov.daniel.data.set;

import com.lubarov.daniel.data.source.IteratorSource;
import com.lubarov.daniel.data.source.Source;

import java.util.Arrays;

public final class ImmutableHashSet<A> extends AbstractImmutableSet<A> {
  private final java.util.HashSet<A> proxy;

  private ImmutableHashSet(java.util.HashSet<A> proxy) {
    this.proxy = proxy;
  }

  public static <A> ImmutableHashSet<A> create() {
    return new ImmutableHashSet<>(new java.util.HashSet<>());
  }

  @SafeVarargs
  public static <A> ImmutableHashSet<A> create(A... values) {
    return new ImmutableHashSet<>(new java.util.HashSet<>(Arrays.asList(values)));
  }

  public static <A> ImmutableHashSet<A> copyOf(Iterable<? extends A> iterable) {
    java.util.HashSet<A> proxy = new java.util.HashSet<>();
    for (A element : iterable)
      proxy.add(element);
    return new ImmutableHashSet<>(proxy);
  }

  @Override
  public boolean contains(A value) {
    return proxy.contains(value);
  }

  @Override
  public Source<A> getEnumerator() {
    return new IteratorSource<>(proxy.iterator());
  }

  @Override
  public int getSize() {
    return proxy.size();
  }
}
