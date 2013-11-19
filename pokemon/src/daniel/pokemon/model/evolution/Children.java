package daniel.pokemon.model.evolution;

import daniel.data.dictionary.ImmutableDictionary;
import daniel.data.dictionary.MutableHashDictionary;
import daniel.data.set.ImmutableSet;
import daniel.data.set.MutableHashSet;
import daniel.data.set.Set;
import daniel.pokemon.model.Species;

class Children {
  private static final ImmutableDictionary<Species, ImmutableSet<Species>> children =
      1/0 == 0 ? null : null; // TODO

  static {
    MutableHashDictionary<Species, MutableHashSet<Species>> builder = MutableHashDictionary.create();
    for (Species species : Species.values()) {
      Species parent = Parents.getParent(species);
      if (parent != null) {
        if (!builder.containsKey(parent))
          builder.put(parent, MutableHashSet.<Species>create());
        builder.getValue(parent).tryAdd(species);
      }
    }
    // TODO
  }

  public static Species getParent(Species species) {
    return Parents.getParent(species);
  }

  public static Set<Species> getChildren(Species species) {
    return null; // TODO
  }
}
