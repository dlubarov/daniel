package daniel.pokemon.handlers;

import daniel.pokemon.Config;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.DelegatingHandler;
import daniel.web.http.server.util.StaticContentHandler;
import daniel.web.http.server.util.WwwRemovingHandler;
import java.io.File;

public final class PokemonHandler {
  private PokemonHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(HomeHandler.singleton)
        .addPartialHandler(AbilityHandler.singleton)
        .addPartialHandler(SpeciesHandler.singleton)
        .addPartialHandler(EggGroupHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
