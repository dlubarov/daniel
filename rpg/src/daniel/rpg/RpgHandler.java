package daniel.rpg;

import daniel.web.http.server.Handler;
import daniel.web.http.server.util.DelegatingHandler;
import daniel.web.http.server.util.StaticContentHandler;
import daniel.web.http.server.util.WwwRemovingHandler;
import java.io.File;

public final class RpgHandler {
  private RpgHandler() {}

  public static Handler getHandler() {
    // TODO: Move from static index.html to something else. Need to handle /.
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
