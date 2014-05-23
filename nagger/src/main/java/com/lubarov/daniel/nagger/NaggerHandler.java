package com.lubarov.daniel.nagger;

import com.lubarov.daniel.nagger.storage.TestData;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.DelegatingHandler;
import com.lubarov.daniel.web.http.server.util.StaticContentHandler;
import com.lubarov.daniel.web.http.server.util.WwwRemovingHandler;

import java.io.File;

public final class NaggerHandler {
  private NaggerHandler() {}

  public static Handler getHandler() {
    TestData.init();
    AlertProcessor.singleton.initialize();
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .addPartialHandler(HomeHandler.singleton)
        .build();
  }
}
