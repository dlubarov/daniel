package daniel.web.http.server;

import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;

public class ExceptionHandlingHandler implements Handler {
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
      Element html = new Element(Tag.HTML,
          new Element(Tag.HEAD,
              new Element(Tag.TITLE, TextNode.rawText("Internal Server Error"))
          ),
          new Element(Tag.BODY,
              new Element(Tag.H1, TextNode.escapedText("Oops.")),
              new Element(Tag.P, TextNode.escapedText("The server encountered an error."))
          )
      );
      Document document = new Document("", html);
      return HttpResponseFactory.htmlResponse(HttpStatus.INTERNAL_SERVER_ERROR, document);
    }
  }
}
