package daniel.pokemon.model.evolution;

public class TradeWithItemEvolution extends EvolutionMethod {
  private final String itemName;

  public TradeWithItemEvolution(String itemName) {
    this.itemName = itemName;
  }

  @Override
  public String toString() {
    return String.format("Trade holding %s", itemName);
  }
}
