package daniel.viewheaders;

import daniel.web.html.Attribute;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.HtmlUtils;
import daniel.web.html.Node;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;

public final class Layout {
  private Layout() {}

  public static Document createDocument(Node... content) {
    Element html = new Element(Tag.HTML, getHead(), getBody(content));
    return new Document("<!DOCTYPE html>", html);
  }

  private static Element getHead() {
    return new Element(Tag.HEAD,
        new Element.Builder(Tag.META)
            .setAttribute(Attribute.HTTP_EQUIV, "Content-type")
            .setAttribute(Attribute.CONTENT, "text/html;charset=UTF-8")
            .build(),
        new Element.Builder(Tag.BASE)
            .setAttribute(Attribute.HREF, Config.getBaseUrl())
            .build(),
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("style.css"),
        new Element(Tag.TITLE, TextNode.rawText("View Headers"))
    );
  }

  private static Element getBody(Node[] content) {
    return new Element(Tag.BODY,
        new Element(Tag.H1,
            new Element.Builder(Tag.A)
                .setAttribute(Attribute.HREF, Config.getBaseUrl())
                .addChild(TextNode.escapedText("View Headers"))
                .build()),
        new Element.Builder(Tag.DIV)
            .setAttribute(Attribute.ID, "content")
            .addChildren(content)
            .build(),
        HtmlUtils.getClearDiv(),
        getFooter()
    );
  }

  private static Element getFooter() {
    return new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "footer")
        .addChild(new Element.Builder(Tag.P)
            .setAttribute(Attribute.CLASS, "fr")
            .addChild(TextNode.escapedText("Valid HTML & CSS"))
            .build())
        .addChild(new Element.Builder(Tag.P)
            .addChild(TextNode.rawText("Copyright &#169; Daniel Lubarov"))
            .build())
        .build();
  }
}
