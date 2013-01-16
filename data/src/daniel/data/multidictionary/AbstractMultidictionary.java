package daniel.data.multidictionary;

import daniel.data.collection.AbstractCollection;
import daniel.data.dictionary.KeyValuePair;

public abstract class AbstractMultidictionary<K, V>
    extends AbstractCollection<KeyValuePair<K, V>>
    implements Multidictionary<K, V> {
  @Override
  public ImmutableMultidictionary<K, V> toImmutable() {
    return ImmutableHashMultitable.copyOf(this);
  }
}
