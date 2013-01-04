package daniel.web.http.server;

import daniel.web.html.Attribute;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;

/**
 * A handler which displays a generic "resource not found" error.
 */
public class StandardNotFoundHandler implements Handler {
  @Override
  public HttpResponse handle(HttpRequest request) {
    Element html = new Element.Builder(Tag.HTML)
        .addChild(new Element(Tag.HEAD,
            new Element.Builder(Tag.META)
                .setAttribute(Attribute.HTTP_EQUIV, "Content-type")
                .setAttribute(Attribute.CONTENT, "text/html;charset=UTF-8")
                .build(),
            new Element(Tag.TITLE, TextNode.rawText("Daniel's Blog"))))
        .addChild(new Element(Tag.BODY,
            new Element(Tag.H1, new Element(Tag.A, TextNode.rawText("Not Found"))),
            new Element(Tag.P,
                TextNode.rawText("The requested resource, "),
                new Element(Tag.STRONG, TextNode.escapedText(request.getResource())),
                TextNode.rawText(", was not found."))))
        .build();

    return HttpResponseFactory.htmlResponse(
        HttpStatus.NOT_FOUND,
        new Document("<!DOCTYPE html>", html));
  }
}
