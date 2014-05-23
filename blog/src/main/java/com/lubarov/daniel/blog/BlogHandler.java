package com.lubarov.daniel.blog;

import com.lubarov.daniel.blog.admin.AdminHandler;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.DelegatingHandler;
import com.lubarov.daniel.web.http.server.util.LineSeparatorRemovingHandler;
import com.lubarov.daniel.web.http.server.util.StaticContentHandler;
import com.lubarov.daniel.web.http.server.util.WwwRemovingHandler;

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
