package daniel.data.multiset;

import daniel.data.collection.Collection;

public interface MutableBag<A> extends Collection<A> {
  public void add(A value);

  public void add(A value, int count);

  public boolean tryRemove(A value);

  public boolean tryRemove(A value, int count);

  public void setCount(A value, int count);
}
