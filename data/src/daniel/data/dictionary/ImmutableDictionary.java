package daniel.data.dictionary;

import daniel.data.set.ImmutableSet;

public interface ImmutableDictionary<K, V>
    extends Dictionary<K, V>, ImmutableSet<KeyValuePair<K, V>> {
}
