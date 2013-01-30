package daniel.web.http.server.util;

import daniel.web.html.AnchorBuilder;
import daniel.web.html.Element;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.HttpResponseFactory;

/**
 * A handler which displays a generic "resource not found" error.
 */
public final class StandardNotFoundHandler implements Handler {
  public static final StandardNotFoundHandler singleton = new StandardNotFoundHandler();

  private StandardNotFoundHandler() {}

  @Override
  public HttpResponse handle(HttpRequest request) {
    Element head = new Element(Tag.HEAD,
        new TitleBuilder().addRawText("Daniel's Blog").build()
    );

    Element heading = new Element(Tag.H1,
        new AnchorBuilder().addEscapedText("Not Found").build()
    );
    Element content = new ParagraphBuilder()
        .addEscapedText("The requested resource, ")
        .addChild(new Element.Builder(Tag.STRONG).addEscapedText(request.getResource()).build())
        .addEscapedText(", was not found.")
        .build();
    Element body = new Element(Tag.BODY, heading, content);

    Element html = new Element.Builder(Tag.HTML)
        .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setAttribute("xml:lang", "en")
        .addChild(head)
        .addChild(body)
        .build();

    return HttpResponseFactory.xhtmlResponse(HttpStatus.NOT_FOUND, html);
  }
}
