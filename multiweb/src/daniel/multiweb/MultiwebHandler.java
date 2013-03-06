package daniel.multiweb;

import daniel.alexresume.AlexResumeHandler;
import daniel.blog.BlogHandler;
import daniel.chat.ChatHandler;
import daniel.rpg.RpgHandler;
import daniel.viewheaders.ViewHeadersHandler;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.HostBasedHandler;

final class MultiwebHandler implements Handler {
  public static final MultiwebHandler singleton = new MultiwebHandler();

  private static final Handler hostBasedHandler = new HostBasedHandler.Builder()
        .addHandlerForHost(".*daniel\\.lubarov\\.com.*", BlogHandler.getHandler())
        .addHandlerForHost(".*alex\\.lubarov\\.com.*", AlexResumeHandler.getHandler())
        .addHandlerForHost(".*viewheaders\\.com.*", ViewHeadersHandler.getHandler())
        .addHandlerForHost(".*jabberings\\.net.*", ChatHandler.getHandler())
        .addHandlerForHost(".*rpg\\.com.*", RpgHandler.getHandler())
        .build();

  private MultiwebHandler() {}

  @Override
  public HttpResponse handle(HttpRequest request) {
    if (request.getResource().equals("/_version"))
      return VersionHandler.singleton.handle(request);
    if (request.getResource().equals("/_update"))
      return UpdateHandler.singlgeton.handle(request);
    return hostBasedHandler.handle(request);
  }
}
