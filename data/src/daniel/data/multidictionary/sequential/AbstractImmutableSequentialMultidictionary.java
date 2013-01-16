package daniel.data.multidictionary.sequential;

public abstract class AbstractImmutableSequentialMultidictionary<K, V>
    extends AbstractSequentialMultidictionary<K, V>
    implements ImmutableSequentialMultidictionary<K, V> {
  @Override
  public ImmutableSequentialMultidictionary<K, V> toImmutable() {
    return this;
  }
}
