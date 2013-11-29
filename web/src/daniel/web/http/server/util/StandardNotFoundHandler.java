package daniel.web.http.server.util;

import daniel.web.html.AnchorBuilder;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.Handler;

/**
 * A handler which displays a generic "resource not found" error.
 */
public final class StandardNotFoundHandler implements Handler {
  public static final StandardNotFoundHandler singleton = new StandardNotFoundHandler();

  private StandardNotFoundHandler() {}

  @Override
  public HttpResponse handle(HttpRequest request) {
    Element style = new Element.Builder(Tag.STYLE)
        .setRawAttribute(Attribute.TYPE, "text/css")
        .addRawText("* { margin: 0; padding: 0; }")
        .addRawText("a { text-decoration: none; }")
        .addRawText("a:hover { text-decoration: underline; }")
        .addRawText("p { margin-top: 0.6em; }")
        .build();

    Element head = new Element(Tag.HEAD,
        new TitleBuilder().addRawText("Daniel's Blog").build(),
        style
    );

    Element heading = new Element(Tag.H1,
        new AnchorBuilder().addEscapedText("Uh oh.").build()
    );
    Element p1 = new ParagraphBuilder()
        .addEscapedText("The requested resource, ")
        .addChild(new Element.Builder(Tag.STRONG)
            .addEscapedText(request.getResource())
            .build())
        .addEscapedText(", was not found. Perhaps I moved it somewhere, and couldn't be bothered to set up a redirect.")
        .build();
    Element p2 = new ParagraphBuilder()
        .addEscapedText("If you can't find what you're looking for, ")
        .addChild(new AnchorBuilder()
            .setHref("mailto:daniel@lubarov.com")
            .addEscapedText("send me an email")
            .build())
        .addEscapedText(".")
        .build();
    Element container = new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.STYLE, "border: 15px solid #444; width: 600px; padding: 2em; margin: 4em auto;")
        .addChild(heading)
        .addChild(p1)
        .addChild(p2)
        .build();
    Element body = new Element.Builder(Tag.BODY)
        .setRawAttribute(Attribute.STYLE, "font-family: 'Trebuchet MS', Arial, sans-serif; color: #444;")
        .addChild(container)
        .build();

    Element html = new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(head)
        .addChild(body)
        .build();

    return HttpResponseFactory.xhtmlResponse(HttpStatus.NOT_FOUND, html);
  }
}
