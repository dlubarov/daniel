package daniel.data.bag;

import daniel.data.collection.Collection;

/**
 * A {@link daniel.data.set.Set}-like structure which can contain duplicate elements.
 */
public interface MutableBag<A> extends Collection<A> {
  public void add(A value);

  public void add(A value, int count);

  public boolean tryRemove(A value);

  public boolean tryRemove(A value, int count);

  public void setCount(A value, int count);
}
