package com.lubarov.daniel.web.http.server.util;

import com.lubarov.daniel.common.Logger;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.html.TitleBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.Handler;

public final class ExceptionHandlingHandler implements Handler {
  private static final Logger logger = Logger.forClass(ExceptionHandlingHandler.class);

  private final Handler delegate;

  public ExceptionHandlingHandler(Handler delegate) {
    this.delegate = delegate;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    try {
      return delegate.handle(request);
    } catch (Exception e) {
      logger.error(e, "Error handling requeset:\n", request);

      Element title = new TitleBuilder()
          .addEscapedText("Internal Server Error")
          .build();
      Element head = new Element(Tag.HEAD, title);

      Element body = new Element(Tag.BODY,
          new Element.Builder(Tag.H1).addEscapedText("Oops.").build(),
          new ParagraphBuilder().addEscapedText("The server encountered an error.").build()
      );

      Element html = new Element.Builder(Tag.HTML)
          .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
          .setRawAttribute("xml:lang", "en")
          .addChild(head)
          .addChild(body)
          .build();

      return HttpResponseFactory.xhtmlResponse(HttpStatus.INTERNAL_SERVER_ERROR, html);
    }
  }
}
