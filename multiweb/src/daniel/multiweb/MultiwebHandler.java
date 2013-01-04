package daniel.multiweb;

import daniel.blog.MainHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.HostBasedHandler;

public class MultiwebHandler {
  private MultiwebHandler() {}

  public static Handler getHandler() {
    return new HostBasedHandler.Builder()
        .addHandlerForHost(".*", MainHandler.getHandler())
        .build();
  }
}
