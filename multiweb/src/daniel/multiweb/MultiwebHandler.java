package daniel.multiweb;

import daniel.alexresume.AlexResumeHandler;
import daniel.blog.BlogHandler;
import daniel.chat.ChatHandler;
import daniel.rpg.RpgHandler;
import daniel.viewheaders.ViewHeadersHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.ExceptionHandlingHandler;
import daniel.web.http.server.util.HostBasedHandler;

final class MultiwebHandler {
  private MultiwebHandler() {}

  public static Handler getHandler() {
    // TODO: Be more restrictive with stuff at beginning and end of host.
    Handler handler = new HostBasedHandler.Builder()
        .addHandlerForHost(".*daniel\\.lubarov\\.com.*", BlogHandler.getHandler())
        .addHandlerForHost(".*alex\\.lubarov\\.com.*", AlexResumeHandler.getHandler())
        .addHandlerForHost(".*viewheaders\\.com.*", ViewHeadersHandler.getHandler())
        .addHandlerForHost(".*jabberings\\.net.*", ChatHandler.getHandler())
        .addHandlerForHost(".*rpg\\.com.*", RpgHandler.getHandler())
        .build();
    return new ExceptionHandlingHandler(handler);
  }
}
