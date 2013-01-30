package daniel.viewheaders;

import daniel.web.http.server.util.WwwRemovingHandler;
import daniel.web.http.server.util.DelegatingHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.StaticContentHandler;
import java.io.File;

public final class ViewHeadersHandler {
  private ViewHeadersHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(HomeHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
