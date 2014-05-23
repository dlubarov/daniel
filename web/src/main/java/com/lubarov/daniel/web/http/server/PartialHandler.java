package com.lubarov.daniel.web.http.server;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;

public interface PartialHandler {
  public Option<HttpResponse> tryHandle(HttpRequest request);
}
