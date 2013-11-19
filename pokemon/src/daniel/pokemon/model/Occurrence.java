package daniel.pokemon.model;

public class Occurrence {
  private final Species species;
  private final Region region;
  private final OccurrenceType type;

  public Occurrence(Species species, Region region, OccurrenceType type) {
    this.species = species;
    this.region = region;
    this.type = type;
  }
}
