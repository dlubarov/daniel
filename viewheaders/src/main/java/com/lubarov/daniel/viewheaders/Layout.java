package com.lubarov.daniel.viewheaders;

import com.lubarov.daniel.web.html.AnchorBuilder;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.HtmlUtils;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.StylesheetUtils;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.html.TitleBuilder;

final class Layout {
  private Layout() {}

  public static Element createDocument(Node... content) {
    return new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(getHead())
        .addChild(getBody(content))
        .build();
  }

  private static Element getHead() {
    Element description = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "description")
        .setRawAttribute(Attribute.CONTENT, "View the HTTP headers that your web browser is sending.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "keywords")
        .setRawAttribute(Attribute.CONTENT, "headers, http, browser")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setRawAttribute(Attribute.HREF, Config.getBaseUrl())
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
            .setRawAttribute(Attribute.ID, "content")
            .addChildren(content)
            .build())
        .addChild(HtmlUtils.getClearDiv())
        .addChild(getFooter())
        .build();
  }

  private static Element getFooter() {
    return new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.ID, "footer")
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
