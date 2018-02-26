package com.lubarov.daniel.conniewedding;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class WeddingHomeHandler implements PartialHandler {
  public static final WeddingHomeHandler singleton = new WeddingHomeHandler();

  private WeddingHomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Element document = WeddingLayout.createDocument(Option.none());
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
