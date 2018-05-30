package com.lubarov.daniel.junkmail;

import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.DelegatingHandler;
import com.lubarov.daniel.web.http.server.util.StaticContentHandler;
import com.lubarov.daniel.web.http.server.util.WwwRemovingHandler;

import java.io.File;

public class JunkMailHandler {
  private JunkMailHandler() {}

  public static Handler getHandler() {
    new Thread(SmtpListener.singleton).start();

    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .addPartialHandler(HomeHandler.singleton)
        .addPartialHandler(InboxHandler.singleton)
        .build();
  }
}
