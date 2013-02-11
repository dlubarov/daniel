package daniel.data.table;

public abstract class AbstractImmutableTable<K, V>
    extends AbstractTable<K, V>
    implements ImmutableTable<K, V> {
  @Override
  public ImmutableTable<K, V> toImmutable() {
    return this;
  }
}
