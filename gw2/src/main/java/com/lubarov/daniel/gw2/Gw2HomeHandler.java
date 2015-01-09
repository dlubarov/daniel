package com.lubarov.daniel.gw2;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.web.html.AnchorBuilder;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class Gw2HomeHandler implements PartialHandler {
  public static final Gw2HomeHandler singleton = new Gw2HomeHandler();

  private Gw2HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Element document = Gw2Layout.createDocument(request, Option.none(), ImmutableArray.create(),
        new ParagraphBuilder()
            .addEscapedText("Hi there! If you're working on a Guild Wars 2 build, you might find this ")
            .addChild(new AnchorBuilder()
                .setHref("/effective-power")
                .setTitle("effective power calculator")
                .addEscapedText("effective power calculator")
                .build())
            .addEscapedText(" useful.").build(),
        new ParagraphBuilder()
            .addEscapedText("I plan to add more calculators, guides and other content, but that's all for now. Please check back later.")
            .build());
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
