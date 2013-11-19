package daniel.pokemon.model;

public enum Move {
  TACKLE(Type.NORMAL, MoveCategory.PHYSICAL, 50, 100, 35),
  ;

  private final Type type;
  private final MoveCategory category;
  private final Integer power;
  private final Integer accuracy;
  private final int pp;

  private Move(Type type, MoveCategory category, Integer power, Integer accuracy, int pp) {
    this.type = type;
    this.category = category;
    this.power = power;
    this.accuracy = accuracy;
    this.pp = pp;
  }
}
