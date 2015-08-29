package com.lubarov.daniel.junkmail;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Element document = null;
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.TEMPORARY_REDIRECT, document));
  }
}
