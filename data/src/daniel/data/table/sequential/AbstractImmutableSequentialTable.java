package daniel.data.table.sequential;

public abstract class AbstractImmutableSequentialTable<K, V>
    extends AbstractSequentialTable<K, V>
    implements ImmutableSequentialTable<K, V> {
  @Override
  public ImmutableSequentialTable<K, V> toImmutable() {
    return this;
  }
}
