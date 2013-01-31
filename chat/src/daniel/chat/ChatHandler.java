package daniel.chat;

import daniel.web.http.server.Handler;
import daniel.web.http.server.util.DelegatingHandler;
import daniel.web.http.server.util.StaticContentHandler;
import daniel.web.http.server.util.WwwRemovingHandler;
import java.io.File;

public final class ChatHandler {
  private ChatHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .addPartialHandler(ChatPageHandler.singleton)
        .build();
  }
}
