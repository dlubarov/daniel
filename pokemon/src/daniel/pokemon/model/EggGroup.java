package daniel.pokemon.model;

public enum EggGroup {
  MONSTER("Monster"),
  HUMAN_LIKE("Human-Like"),
  WATER_1("Water 1"),
  WATER_3("Water 3"),
  BUG("Bug"),
  MINERAL("Mineral"),
  FLYING("Flying"),
  AMORPHOUS("Amorphous"),
  FIELD("Field"),
  WATER_2("Water 2"),
  FAIRY("Fairy"),
  DITTO("Ditto"),
  GRASS("Grass"),
  DRAGON("Dragon"),
  UNDISCOVERED("Undiscovered"),
  GENDER_UNKNOWN("Gender Unknown"),
  ;

  private final String visibleName;

  private EggGroup(String visibleName) {
    this.visibleName = visibleName;
  }

  @Override
  public String toString() {
    return visibleName;
  }
}
