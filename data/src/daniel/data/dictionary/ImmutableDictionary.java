package daniel.data.dictionary;

import daniel.data.set.ImmutableSet;

/**
 * A {@link Dictionary} whose elements never change.
 */
public interface ImmutableDictionary<K, V>
    extends Dictionary<K, V>, ImmutableSet<KeyValuePair<K, V>> {
}
