package com.lubarov.daniel.conniewedding;

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

    Element directionsLink = new AnchorBuilder()
        .setHref("https://maps.google.com/?q=1+Doubletree,+Rohnert+Park,+CA+94928&entry=gmail&source=g")
        .setTitle("Directions to Presidio Golfcourse & Clubhouse")
        .addEscapedText("Directions via Google Maps")
        .build();

    Element intro = new ParagraphBuilder()
        .addEscapedText("The ceremony will start at 4:00 pm at the outdoor garden in the DoubleTree hotel. The reception will begin at 5 pm in the Chardonnay ballroom.")
        .build();

    Element address = new ParagraphBuilder()
        .addEscapedText("DoubleTree by Hilton Hotel")
        .addChild(new Element(Tag.BR))
        .addEscapedText("1 Doubletree")
        .addChild(new Element(Tag.BR))
        .addEscapedText("Rohnert Park, CA 94928")
        .addChild(new Element(Tag.BR))
        .addChild(directionsLink)
        .build();

    Element document = WeddingLayout.createDocument(
        Option.some("Details"),
        address,
        intro);

    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
