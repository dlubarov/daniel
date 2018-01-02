package com.lubarov.daniel.wedding;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class WeddingVenueHandler implements PartialHandler {
  public static final WeddingVenueHandler singleton = new WeddingVenueHandler();

  private WeddingVenueHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/venue"))
      return Option.none();

    Element document = WeddingLayout.createDocument(
        Option.some("Venue"),
        new ParagraphBuilder()
            .addEscapedText("Presidio")
            .build());
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
