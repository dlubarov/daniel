package com.lubarov.daniel.multiweb;

import com.lubarov.daniel.alexresume.AlexResumeHandler;
import com.lubarov.daniel.blog.BlogHandler;
import com.lubarov.daniel.chat.ChatHandler;
import com.lubarov.daniel.wedding.Gw2Handler;
import com.lubarov.daniel.wedding.WeddingHandler;
import com.lubarov.daniel.junkmail.JunkMailHandler;
import com.lubarov.daniel.nagger.NaggerHandler;
import com.lubarov.daniel.viewheaders.ViewHeadersHandler;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HostBasedHandler;

final class MultiwebHandler implements Handler {
  public static final MultiwebHandler singleton = new MultiwebHandler();

  private static final Handler hostBasedHandler = new HostBasedHandler.Builder()
      .addHandlerForHost(".*nagger\\.daniel\\.lubarov\\.com.*", NaggerHandler.getHandler())
      .addHandlerForHost(".*danielvi\\.com.*", WeddingHandler.getHandler())
      .addHandlerForHost(".*daniel\\.lubarov\\.com.*", BlogHandler.getHandler())
      .addHandlerForHost(".*alex\\.lubarov\\.com.*", AlexResumeHandler.getHandler())
      .addHandlerForHost(".*viewheaders\\.com.*", ViewHeadersHandler.getHandler())
      .addHandlerForHost(".*jabberings\\.net.*", ChatHandler.getHandler())
      .addHandlerForHost(".*gw2tools\\.net.*", Gw2Handler.getHandler())
      .addHandlerForHost(".*baggageman\\.com.*", JunkMailHandler.getHandler())
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
