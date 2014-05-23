package com.lubarov.daniel.multiweb;

import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class VersionHandler implements Handler {
  public static final VersionHandler singleton = new VersionHandler();

  private VersionHandler() {}

  @Override
  public HttpResponse handle(HttpRequest request) {
    String location = "https://github.com/dlubarov/daniel/commit/" + getVersion();
    return HttpResponseFactory.redirectSameMethod(location);
  }

  private static String getVersion() {
    String version = VersionHandler.class.getPackage().getImplementationVersion();
    return version != null ? version : "unknown";
  }
}
