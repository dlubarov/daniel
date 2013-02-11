package daniel.data.collection;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashDictionary;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.data.source.Source;

final class EqualityTester {
  private EqualityTester() {}

  public static boolean areCollectionsEqual(Collection<?> a, Collection<?> b) {
    if (a.getSize() != b.getSize())
      return false;

    if (a instanceof Sequence<?> && b instanceof Sequence<?>)
      return areEqualInOrder(a, b);
    else
      return areEqualAnyOrder(a, b);
  }

  private static boolean areEqualInOrder(Collection<?> a, Collection<?> b) {
    Source<?> aEnumerator = a.getEnumerator();
    Source<?> bEnumerator = b.getEnumerator();
    for (;;) {
      Option<?> optAElement = aEnumerator.tryTake();
      Option<?> optBElement = bEnumerator.tryTake();

      if (optAElement.isEmpty() && optBElement.isEmpty())
        return true;
      if (optAElement.isEmpty() || optBElement.isEmpty())
        return false;
      if (!optAElement.getOrThrow().equals(optBElement.getOrThrow()))
        return false;
    }
  }

  private static boolean areEqualAnyOrder(Collection<?> a, Collection<?> b) {
    MutableHashDictionary<Object, Integer> aCounts = MutableHashDictionary.create();
    MutableHashDictionary<Object, Integer> bCounts = MutableHashDictionary.create();
    for (Object element : a)
      aCounts.put(element, aCounts.tryGetValue(element).getOrDefault(0) + 1);
    for (Object element : b)
      bCounts.put(element, bCounts.tryGetValue(element).getOrDefault(0) + 1);

    if (aCounts.getSize() != bCounts.getSize())
      return false;
    for (KeyValuePair<Object, Integer> aCount : aCounts) {
      int bCount = bCounts.tryGetValue(aCount.getKey()).getOrDefault(0);
      if (aCount.getValue() != bCount)
        return false;
    }
    return true;
  }
}
