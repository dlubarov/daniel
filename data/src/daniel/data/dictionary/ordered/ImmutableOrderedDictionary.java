package daniel.data.dictionary.ordered;

import daniel.data.dictionary.ImmutableDictionary;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.function.Function;
import daniel.data.set.ordered.ImmutableOrderedSet;

public interface ImmutableOrderedDictionary<K, V> extends
    OrderedDictionary<K, V>, ImmutableDictionary<K, V>, ImmutableOrderedSet<KeyValuePair<K, V>> {
  @Override
  public ImmutableOrderedDictionary<K, V> filter(Function<? super KeyValuePair<K, V>, Boolean> predicate);

  @Override
  public ImmutableOrderedDictionary<K, V> toImmutable();
}
