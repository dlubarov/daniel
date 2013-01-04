package daniel.data.dictionary.ordered;

import daniel.data.dictionary.Dictionary;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.function.Function;
import daniel.data.set.ordered.OrderedSet;

public interface OrderedDictionary<K, V> extends Dictionary<K, V>, OrderedSet<KeyValuePair<K, V>> {
  @Override
  public ImmutableOrderedDictionary<K, V> filter(Function<? super KeyValuePair<K, V>, Boolean> predicate);

  @Override
  public ImmutableOrderedDictionary<K, V> toImmutable();
}
