package daniel.viewheaders;

import daniel.web.html.AnchorBuilder;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.HtmlUtils;
import daniel.web.html.Node;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;

final class Layout {
  private Layout() {}

  public static Element createDocument(Node... content) {
    return new Element.Builder(Tag.HTML)
        .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setAttribute("xml:lang", "en")
        .addChild(getHead())
        .addChild(getBody(content))
        .build();
  }

  private static Element getHead() {
    Element description = new Element.Builder(Tag.META)
        .setAttribute(Attribute.NAME, "description")
        .setAttribute(Attribute.CONTENT, "View the HTTP headers that your web browser is sending.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setAttribute(Attribute.NAME, "keywords")
        .setAttribute(Attribute.CONTENT, "headers, http, browser")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setAttribute(Attribute.HREF, Config.getBaseUrl())
        .build();
    return new Element(Tag.HEAD,
        description, keywords, base,
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("style.css"),
        new TitleBuilder().addEscapedText("View Headers").build()
    );
  }

  private static Element getBody(Node[] content) {
    return new Element.Builder(Tag.BODY)
        .addChild(new Element(Tag.H1,
            new AnchorBuilder()
                .setHref(Config.getBaseUrl())
                .addEscapedText("View Headers")
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
        .addChild(new ParagraphBuilder()
            .setClass("fr")
            .addEscapedText("Valid HTML & CSS")
            .build())
        .addChild(new ParagraphBuilder()
            .addRawText("Copyright &#169; Daniel Lubarov")
            .build())
        .build();
  }
}
