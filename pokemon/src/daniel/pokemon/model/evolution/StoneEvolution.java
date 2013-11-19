package daniel.pokemon.model.evolution;

public class StoneEvolution extends EvolutionMethod {
  private final String stoneName;

  public StoneEvolution(String stoneName) {
    this.stoneName = stoneName;
  }

  @Override
  public String toString() {
    return stoneName;
  }
}
