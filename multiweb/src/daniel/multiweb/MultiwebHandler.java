package daniel.multiweb;

import daniel.blog.BlogHandler;
import daniel.chat.ChatHandler;
import daniel.chat.ChatWebSocketHandler;
import daniel.chat.ChatWebSocketHandler;
import daniel.cms.CmsHandler;
import daniel.viewheaders.ViewHeadersHandler;
import daniel.web.http.server.util.ExceptionHandlingHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.HostBasedHandler;

final class MultiwebHandler {
  private MultiwebHandler() {}

  public static Handler getHandler() {
    Handler handler = new HostBasedHandler.Builder()
        .addHandlerForHost(".*daniel.lubarov\\.com.*", BlogHandler.getHandler())
        .addHandlerForHost(".*viewheaders\\.com.*", ViewHeadersHandler.getHandler())
        .addHandlerForHost(".*jabberings\\.net.*", ChatHandler.getHandler())
        .addHandlerForHost(".*acquisitive\\.net.*", CmsHandler.getHandler())
        .build();
    return new ExceptionHandlingHandler(handler);
  }
}
