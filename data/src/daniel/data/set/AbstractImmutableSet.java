package daniel.data.set;

public abstract class AbstractImmutableSet<A> extends AbstractSet<A> implements ImmutableSet<A> {
  @Override
  public ImmutableSet<A> toImmutable() {
    return this;
  }
}
