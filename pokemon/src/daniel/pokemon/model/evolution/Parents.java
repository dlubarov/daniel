package daniel.pokemon.model.evolution;

import daniel.pokemon.model.Species;
import java.util.HashMap;
import java.util.Map;

class Parents {
  private static final Map<Species, Species> parents;

  static {
    parents = new HashMap<>();
    parents.put(Species.IVYSAUR, Species.BULBASAUR);
    parents.put(Species.VENUSAUR, Species.IVYSAUR);
  }

  public static Species getParent(Species species) {
    return parents.get(species);
  }
}
