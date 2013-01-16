package daniel.blog;

import daniel.blog.admin.AdminHandler;
import daniel.web.http.server.DelegatingHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.StaticContentHandler;
import daniel.web.http.server.WwwRemover;
import java.io.File;

public final class BlogHandler {
  private BlogHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemover.singleton)
        .addPartialHandler(HomeHandler.singleton)
        .addPartialHandler(ViewPostHandler.singleton)
        .addPartialHandler(AdminHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder().setContentRoot(
            new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
