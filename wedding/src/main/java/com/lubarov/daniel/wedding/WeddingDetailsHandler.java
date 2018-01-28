package com.lubarov.daniel.wedding;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.html.AnchorBuilder;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class WeddingDetailsHandler implements PartialHandler {
  public static final WeddingDetailsHandler singleton = new WeddingDetailsHandler();

  private WeddingDetailsHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/details"))
      return Option.none();

    Element venueLink = new AnchorBuilder()
        .setHref("http://www.presidiogolf.com/")
        .setTitle("Presidio Golf")
        .addEscapedText("Website")
        .build();

    Element directionsLink = new AnchorBuilder()
        .setHref("http://maps.google.com/maps?f=q&hl=en&q=300+Finley+Rd,+94129&ie=UTF8&om=1&ll=37.791,-122.458935&spn=0.015736,0.042915")
        .setTitle("Directions to Presidio Golfcourse & Clubhouse")
        .addEscapedText("Directions")
        .build();

    Element intro = new ParagraphBuilder()
        .addEscapedText("The ceremony will start at 4:30pm, outdoors by the golf course. It could be windy, so we recommend bringing a jacket! The reception will be indoors and will begin around 5pm.")
        .build();

    Element address = new ParagraphBuilder()
        .addEscapedText("Presidio Golf Course & Clubhouse")
        .addChild(new Element(Tag.BR))
        .addEscapedText("300 Finley Rd")
        .addChild(new Element(Tag.BR))
        .addEscapedText("San Francisco, CA 94129")
        .addChild(new Element(Tag.BR))
        .addChild(venueLink).addEscapedText(" · ").addChild(directionsLink)
        .build();

    Element document = WeddingLayout.createDocument(
        Option.some("Details"),
        address,
        intro);

    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
