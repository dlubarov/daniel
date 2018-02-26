package com.lubarov.daniel.conniewedding;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.html.AnchorBuilder;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class WeddingGiftsHandler implements PartialHandler {
  public static final WeddingGiftsHandler singleton = new WeddingGiftsHandler();

  private WeddingGiftsHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/gifts"))
      return Option.none();

    Element intro = new ParagraphBuilder()
        .addEscapedText("")
        .build();

    Element document = WeddingLayout.createDocument(Option.some("Gifts"), intro);
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
