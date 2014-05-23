package com.lubarov.daniel.alexresume;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.DelegatingHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;
import com.lubarov.daniel.web.http.server.util.StaticContentHandler;
import com.lubarov.daniel.web.http.server.util.WwwRemovingHandler;

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
        .addPartialHandler(request -> {
          String location = String.format("%s/index.html", Config.getBaseUrl());
          return Option.some(HttpResponseFactory.permanentRedirect(location));
        })
        .build();
  }
}
