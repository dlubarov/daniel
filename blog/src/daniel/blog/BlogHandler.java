package daniel.blog;

import daniel.blog.admin.AdminHandler;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.DelegatingHandler;
import daniel.web.http.server.util.LineSeparatorRemovingHandler;
import daniel.web.http.server.util.StaticContentHandler;
import daniel.web.http.server.util.WwwRemovingHandler;
import java.io.File;

public final class BlogHandler {
  private BlogHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(LineSeparatorRemovingHandler.singleton)
        .addPartialHandler(HomeHandler.singleton)
        .addPartialHandler(PostHandler.singleton)
        .addPartialHandler(AdminHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
