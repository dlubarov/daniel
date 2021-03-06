package com.lubarov.daniel.wedding;

import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.DelegatingHandler;
import com.lubarov.daniel.web.http.server.util.LineSeparatorRemovingHandler;
import com.lubarov.daniel.web.http.server.util.StaticContentHandler;
import com.lubarov.daniel.web.http.server.util.WwwRemovingHandler;
import com.lubarov.daniel.wedding.admin.AdminHandler;

import java.io.File;

public class WeddingHandler {
  private WeddingHandler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(LineSeparatorRemovingHandler.singleton)
        .addPartialHandler(WeddingHomeHandler.singleton)
        .addPartialHandler(WeddingDetailsHandler.singleton)
        .addPartialHandler(WeddingGiftsHandler.singleton)
        .addPartialHandler(WeddingPhotosHandler.singleton)
        .addPartialHandler(WeddingRSVPHandler.singleton)
        .addPartialHandler(AdminHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(WeddingConfig.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
