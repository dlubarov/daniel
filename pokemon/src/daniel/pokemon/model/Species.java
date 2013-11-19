package daniel.pokemon.model;

import daniel.data.set.ImmutableHashSet;
import daniel.data.set.Set;

public enum Species {
  BULBASAUR(1,
      s(Type.GRASS, Type.POISON),
      new Stats(45, 49, 49, 65, 65, 45),
      new Stats(0, 0, 0, 1, 0, 0),
      s(Ability.OVERGROW, Ability.CHLOROPHYLL),
      s(EggGroup.MONSTER, EggGroup.GRASS)),
  IVYSAUR(2,
      s(Type.GRASS, Type.POISON),
      new Stats(45, 49, 49, 65, 65, 45),
      new Stats(0, 0, 0, 1, 1, 0),
      s(Ability.OVERGROW, Ability.CHLOROPHYLL),
      s(EggGroup.MONSTER, EggGroup.GRASS)),
  VENUSAUR(3,
      s(Type.GRASS, Type.POISON),
      new Stats(45, 49, 49, 65, 65, 45),
      new Stats(0, 0, 0, 2, 1, 0),
      s(Ability.OVERGROW, Ability.CHLOROPHYLL),
      s(EggGroup.MONSTER, EggGroup.GRASS)),
  SALAMENCE(373,
      s(Type.DRAGON, Type.FLYING),
      new Stats(95, 135, 80, 110, 80, 100),
      new Stats(0, 3, 0, 0, 0, 0),
      s(Ability.INTIMIDATE, Ability.MOXIE),
      s(EggGroup.DRAGON)),
  GARCHOMP(445,
      s(Type.DRAGON, Type.GROUND),
      new Stats(108, 130, 95, 80, 85, 102),
      new Stats(0, 3, 0, 0, 0, 0),
      s(Ability.SAND_VEIL, Ability.ROUGH_SKIN),
      s(EggGroup.MONSTER, EggGroup.DRAGON)),
  HAXORUS(612,
      s(Type.DRAGON),
      new Stats(76, 147, 90, 60, 70, 97),
      new Stats(0, 3, 0, 0, 0, 0),
      s(Ability.RIVALRY, Ability.MOLD_BREAKER, Ability.UNNERVE),
      s(EggGroup.MONSTER, EggGroup.DRAGON)),
  HYDREIGON(635,
      s(Type.DARK, Type.DRAGON),
      new Stats(92, 105, 90, 125, 90, 98),
      new Stats(0, 0, 0, 3, 0, 0),
      s(Ability.LEVITATE),
      s(EggGroup.DRAGON)),
  ;

  private final int nationalPokedexNumber;
  private final Set<Type> types;
  private final Stats baseStats;
  private final Stats effortValues;
  private final Set<Ability> abilities;
  private final Set<EggGroup> eggGroups;

  private Species(int nationalPokedexNumber, Set<Type> types, Stats baseStats, Stats effortValues,
                  Set<Ability> abilities, Set<EggGroup> eggGroups) {
    this.nationalPokedexNumber = nationalPokedexNumber;
    this.types = types;
    this.baseStats = baseStats;
    this.effortValues = effortValues;
    this.abilities = abilities;
    this.eggGroups = eggGroups;
  }

  private static <T> Set<T> s(T... elems) {
    return ImmutableHashSet.create(elems);
  }

  @Override
  public String toString() {
    return name().charAt(0) + name().substring(1).toLowerCase();
  }
}
