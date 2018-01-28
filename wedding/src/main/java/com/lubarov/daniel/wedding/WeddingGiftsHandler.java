package com.lubarov.daniel.wedding;

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

    Element honeyFund = new AnchorBuilder()
        .setHref("https://www.honeyfund.com/wedding/DanielVi")
        .setTitle("Honeyfund")
        .addEscapedText("Honeyfund page")
        .build();

    Element intro = new ParagraphBuilder()
        .addEscapedText("Gifts are totally optional; you can bring something if you like but it isn't expected. We also set up a ")
        .addChild(honeyFund)
        .addEscapedText(" if you'd like to help fund our honeymoon in Europe!")
        .build();

    Element document = WeddingLayout.createDocument(Option.some("Gifts"), intro);
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
