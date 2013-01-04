package daniel.blog;

import daniel.web.http.server.DelegatingHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.StaticContentHandler;
import java.io.File;

public final class MainHandler {
  private MainHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(new HomeHandler())
        .addPartialHandler(ViewPostHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File("/Users/dlubarov/Development/daniel/blog/static/"))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
