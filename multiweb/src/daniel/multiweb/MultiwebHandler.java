package daniel.multiweb;

import daniel.blog.BlogHandler;
import daniel.viewheaders.ViewHeadersHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.HostBasedHandler;

public final class MultiwebHandler {
  private MultiwebHandler() {}

  public static Handler getHandler() {
    return new HostBasedHandler.Builder()
        .addHandlerForHost(".*daniel.lubarov\\.com.*", BlogHandler.getHandler())
        .addHandlerForHost(".*viewheaders\\.com.*", ViewHeadersHandler.getHandler())
        .build();
  }
}
