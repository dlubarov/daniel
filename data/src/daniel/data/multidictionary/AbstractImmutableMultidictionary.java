package daniel.data.multidictionary;

public abstract class AbstractImmutableMultidictionary<K, V>
    extends AbstractMultidictionary<K, V>
    implements ImmutableMultidictionary<K, V> {
  @Override
  public ImmutableMultidictionary<K, V> toImmutable() {
    return this;
  }
}
