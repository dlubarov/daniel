package daniel.blog;

import daniel.web.html.Attribute;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.HtmlUtils;
import daniel.web.html.Node;
import daniel.web.html.Stylesheets;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;

public final class Layout {
  private Layout() {}

  public static Document createDocument(Node... content) {
    Element html = new Element.Builder(Tag.HTML)
        .addChild(getHead())
        .addChild(getBody(content))
        .build();
    return new Document("<!DOCTYPE html>", html);
  }

  private static Element getHead() {
    return new Element(Tag.HEAD,
        new Element.Builder(Tag.META)
            .setAttribute(Attribute.HTTP_EQUIV, "Content-type")
            .setAttribute(Attribute.CONTENT, "text/html;charset=UTF-8")
            .build(),
        new Element.Builder(Tag.BASE)
            .setAttribute(Attribute.HREF, Config.homeUrl)
            .build(),
        Stylesheets.createCssLink("style.css"),
        new Element(Tag.TITLE, TextNode.rawText("Daniel's Blog"))
    );
  }

  private static Element getBody(Node[] content) {
    return new Element(Tag.BODY, new Element(Tag.H1,
        new Element.Builder(Tag.A)
            .setAttribute(Attribute.HREF, Config.homeUrl)
            .addChild(TextNode.escapedText("Daniel's Blog"))
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
            .addChild(TextNode.rawText("Copyright &copy; Daniel Lubarov"))
            .build())
        .build();
  }
}
