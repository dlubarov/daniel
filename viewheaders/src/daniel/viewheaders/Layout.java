package daniel.viewheaders;

import daniel.web.html.Attribute;
import daniel.web.html.Xhtml5Document;
import daniel.web.html.Element;
import daniel.web.html.HtmlUtils;
import daniel.web.html.Node;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;

public final class Layout {
  private Layout() {}

  public static Xhtml5Document createDocument(Node... content) {
    Element html = new Element.Builder(Tag.HTML)
        .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setAttribute("xml:lang", "en")
        .addChild(getHead())
        .addChild(getBody(content))
        .build();
    return new Xhtml5Document(html);
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
    return new Element.Builder(Tag.BODY)
        .addChild(new Element(Tag.H1,
            new Element.Builder(Tag.A).setAttribute(Attribute.HREF, Config.getBaseUrl())
                .addChild(TextNode.escapedText("View Headers"))
                .build()))
        .addChild(new Element.Builder(Tag.DIV)
            .setAttribute(Attribute.ID, "content")
            .addChildren(content)
            .build())
        .addChild(HtmlUtils.getClearDiv())
        .addChild(getFooter())
        .build();
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
