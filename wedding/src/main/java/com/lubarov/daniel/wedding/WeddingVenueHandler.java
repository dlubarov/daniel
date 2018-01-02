package com.lubarov.daniel.wedding;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
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

    Element address = new ParagraphBuilder()
        .addEscapedText("Presidio Golf Course & Clubhouse")
        .addChild(new Element(Tag.BR))
        .addEscapedText("300 Finley Rd")
        .addChild(new Element(Tag.BR))
        .addEscapedText("San Francisco, CA 94129")
        .build();

    // https://www.google.com/maps/place/Presidio+Cafe/@37.7905772,-122.462061,17z/data=!3m1!4b1!4m5!3m4!1s0x808ff1ca82a1acd9:0x97bdbbbf8404d9f2!8m2!3d37.7905772!4d-122.4598723

    Element googleMaps = new Element.Builder(Tag.IFRAME)
        .setRawAttribute(Attribute.SRC, "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3152.977068265033!2d-122.46206098468198!3d37.79057717975672!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x808ff1ca82a1acd9%3A0x97bdbbbf8404d9f2!2sPresidio+Cafe!5e0!3m2!1sen!2sus!4v1514877454577")
        .setRawAttribute(Attribute.WIDTH, "600")
        .setRawAttribute(Attribute.HEIGHT, "450")
        .setRawAttribute("frameborder", "0")
        .setRawAttribute(Attribute.STYLE, "border: 0")
        .setRawAttribute("allowfullscreen", "allowfullscreen")
        .build();

    Element document = WeddingLayout.createDocument(
        Option.some("Venue"),
        address,
        googleMaps);

    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
