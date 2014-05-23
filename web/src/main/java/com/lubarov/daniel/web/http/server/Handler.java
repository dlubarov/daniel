package com.lubarov.daniel.web.http.server;

import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;

public interface Handler {
  public HttpResponse handle(HttpRequest request);
}
