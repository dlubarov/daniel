package daniel.cms;

import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;

public final class Layout {
  private Layout() {}

  public static Element createHtml(Node... content) {
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
        .setAttribute(Attribute.CONTENT, "A content management system written in Java.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setAttribute(Attribute.NAME, "keywords")
        .setAttribute(Attribute.CONTENT, "java, cms, forum, wiki, account, session")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setAttribute(Attribute.HREF, Config.getBaseUrl())
        .build();
    return new Element(Tag.HEAD,
        description, keywords, base,
        new TitleBuilder().addRawText("Java CMS").build(),
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("style.css")
    );
  }

  private static Element getBody(Node... content) {
    return new Element.Builder(Tag.DIV)
        .addChildren(content)
        .build();
  }
}
