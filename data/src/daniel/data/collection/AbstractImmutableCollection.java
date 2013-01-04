package daniel.data.collection;

public abstract class AbstractImmutableCollection<A>
    extends AbstractCollection<A>
    implements ImmutableCollection<A> {
  @Override
  public ImmutableCollection<A> toImmutable() {
    return this;
  }
}
