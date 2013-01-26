package daniel.web.http.server;

import daniel.web.html.Xhtml5Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;

public final class ExceptionHandlingHandler implements Handler {
  private final Handler delegate;

  public ExceptionHandlingHandler(Handler delegate) {
    this.delegate = delegate;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    try {
      return delegate.handle(request);
    } catch (Exception e) {
      e.printStackTrace();

      Element title = new Element(Tag.TITLE, TextNode.rawText("Internal Server Error"));
      Element head = new Element(Tag.HEAD, title);

      Element body = new Element(Tag.BODY,
          new Element(Tag.H1, TextNode.escapedText("Oops.")),
          new Element(Tag.P, TextNode.escapedText("The server encountered an error."))
      );

      Element html = new Element.Builder(Tag.HTML)
          .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
          .setAttribute("xml:lang", "en")
          .addChild(head)
          .addChild(body)
          .build();

      return HttpResponseFactory.xhtmlResponse(HttpStatus.INTERNAL_SERVER_ERROR, html);
    }
  }
}
