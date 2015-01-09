package com.lubarov.daniel.gw2;

import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.DelegatingHandler;
import com.lubarov.daniel.web.http.server.util.LineSeparatorRemovingHandler;
import com.lubarov.daniel.web.http.server.util.StaticContentHandler;
import com.lubarov.daniel.web.http.server.util.WwwRemovingHandler;

import java.io.File;

public class Gw2Handler {
  private Gw2Handler() {}

  public static Handler getHandler() {
    return new DelegatingHandler.Builder()
        .addPartialHandler(WwwRemovingHandler.singleton)
        .addPartialHandler(LineSeparatorRemovingHandler.singleton)
        .addPartialHandler(Gw2HomeHandler.singleton)
        .addPartialHandler(EffectivePowerHandler.singleton)
        .addPartialHandler(new StaticContentHandler.Builder()
            .setContentRoot(new File(Gw2Config.getStaticContentRoot()))
            .addCommonContentTypes()
            .build())
        .build();
  }
}
