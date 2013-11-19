package daniel.pokemon.model;

public class Stats {
  private final int hp;
  private final int attack;
  private final int defense;
  private final int specialAttack;
  private final int specialDefense;
  private final int speed;

  public Stats(int hp, int attack, int defense, int specialAttack, int specialDefense, int speed) {
    this.hp = hp;
    this.attack = attack;
    this.defense = defense;
    this.specialAttack = specialAttack;
    this.specialDefense = specialDefense;
    this.speed = speed;
  }

  public int getHp() {
    return hp;
  }

  public int getAttack() {
    return attack;
  }

  public int getDefense() {
    return defense;
  }

  public int getSpecialAttack() {
    return specialAttack;
  }

  public int getSpecialDefense() {
    return specialDefense;
  }

  public int getSpeed() {
    return speed;
  }

  public int getTotal() {
    return hp + attack + defense + specialAttack + specialDefense + speed;
  }
}
