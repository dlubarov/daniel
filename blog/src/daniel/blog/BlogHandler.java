package daniel.blog;

import daniel.blog.admin.AdminHandler;
import daniel.data.option.Option;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.server.Handler;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.DelegatingHandler;
import daniel.web.http.server.util.StaticContentHandler;
import daniel.web.http.server.util.VersionHandler;
import daniel.web.http.server.util.WwwRemovingHandler;
import java.io.File;

public final class BlogHandler {
  private BlogHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(HomeHandler.singleton)
        .addPartialHandler(new PartialHandler() {
          @Override public Option<HttpResponse> tryHandle(HttpRequest request) {
            return request.getResource().equals("/version")
                ? Option.some(VersionHandler.singleton.handle(request))
                : Option.<HttpResponse>none();
          }
        })
        .addPartialHandler(PostHandler.singleton)
        .addPartialHandler(AdminHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
