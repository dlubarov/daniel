package daniel.viewheaders;

import daniel.web.http.server.DelegatingHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.StaticContentHandler;
import daniel.web.http.server.WwwRemover;
import java.io.File;

public final class ViewHeadersHandler {
  private ViewHeadersHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemover.singleton)
        .addPartialHandler(HomeHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
