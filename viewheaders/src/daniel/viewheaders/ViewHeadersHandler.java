package daniel.viewheaders;

import daniel.web.http.server.DelegatingHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.StaticContentHandler;
import java.io.File;

public final class ViewHeadersHandler {
  private ViewHeadersHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(HomeHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File("/Users/dlubarov/Development/daniel/viewheaders/static/"))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
