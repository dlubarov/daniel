package daniel.data.multidictionary;

import daniel.data.collection.ImmutableCollection;
import daniel.data.dictionary.KeyValuePair;

public interface ImmutableMultidictionary<K, V>
    extends Multidictionary<K, V>, ImmutableCollection<KeyValuePair<K, V>> {
}
