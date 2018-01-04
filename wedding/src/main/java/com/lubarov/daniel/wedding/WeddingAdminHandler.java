package com.lubarov.daniel.wedding;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class WeddingAdminHandler implements PartialHandler {
  public static final WeddingAdminHandler singleton = new WeddingAdminHandler();

  private WeddingAdminHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/admin"))
      return Option.none();

    Element intro = new ParagraphBuilder()
        .addEscapedText("Coming soon!")
        .build();

    Element document = WeddingLayout.createDocument(
        Option.some("Admin"),
        intro);

    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
