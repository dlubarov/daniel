package daniel.pokemon.model.evolution;

public class LevelEvolution extends EvolutionMethod {
  private final int level;

  public LevelEvolution(int level) {
    this.level = level;
  }

  @Override
  public String toString() {
    return String.format("level %d", level);
  }
}
