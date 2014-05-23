package com.lubarov.daniel.chat;

import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.DelegatingHandler;
import com.lubarov.daniel.web.http.server.util.StaticContentHandler;
import com.lubarov.daniel.web.http.server.util.WwwRemovingHandler;

import java.io.File;

public final class ChatHandler {
  private ChatHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .addPartialHandler(ChatPageHandler.singleton)
        .build();
  }
}
