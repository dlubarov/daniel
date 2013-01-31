package daniel.data.multiset;

import daniel.data.collection.AbstractCollection;

public abstract class AbstractMutableMultiset<A>
    extends AbstractCollection<A>
    implements MutableMultiset<A> {
  @Override
  public abstract int getCount(A value);

  @Override
  public abstract void setCount(A value, int count);

  public void add(A value) {
    add(value, 1);
  }

  public void add(A value, int count) {
    setCount(value, getCount(value) + count);
  }

  public boolean tryRemove(A value) {
    return tryRemove(value, 1);
  }

  public boolean tryRemove(A value, int count) {
    if (getCount(value) < count)
      return false;
    setCount(value, getCount(value) - count);
    return true;
  }
}
