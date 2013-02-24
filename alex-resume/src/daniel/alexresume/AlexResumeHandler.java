package daniel.alexresume;

import daniel.data.option.Option;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.server.Handler;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.DelegatingHandler;
import daniel.web.http.server.util.HttpResponseFactory;
import daniel.web.http.server.util.StaticContentHandler;
import daniel.web.http.server.util.WwwRemovingHandler;
import java.io.File;

public final class AlexResumeHandler {
  private AlexResumeHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .addPartialHandler(new PartialHandler() {
          @Override public Option<HttpResponse> tryHandle(HttpRequest request) {
            String location = String.format("%s/index.html", Config.getBaseUrl());
            return Option.some(HttpResponseFactory.permanentRedirect(location));
          }
        })
        .build();
  }
}
