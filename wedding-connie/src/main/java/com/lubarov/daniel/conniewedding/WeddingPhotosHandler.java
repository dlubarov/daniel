package com.lubarov.daniel.conniewedding;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class WeddingPhotosHandler implements PartialHandler {
  public static final WeddingPhotosHandler singleton = new WeddingPhotosHandler();

  private WeddingPhotosHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/photos"))
      return Option.none();

    Element intro = new ParagraphBuilder()
        .addEscapedText("Coming soon!")
        .build();

    Element document = WeddingLayout.createDocument(
        Option.some("Photos"),
        intro);

    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
