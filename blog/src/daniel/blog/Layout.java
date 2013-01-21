package daniel.blog;

import daniel.data.option.Option;
import daniel.web.html.Attribute;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.HtmlUtils;
import daniel.web.html.JavaScriptUtils;
import daniel.web.html.Node;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;

public final class Layout {
  private Layout() {}

  public static Document createDocument(Option<String> title,
      Option<String> subtitle, Node... content) {
    Element html = new Element.Builder(Tag.HTML)
        .addChild(getHead())
        .addChild(getBody(title, subtitle, content))
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
            .setAttribute(Attribute.HREF, Config.getBaseUrl())
            .build(),
        new Element(Tag.TITLE, TextNode.rawText("Daniel's Blog")),
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("style.css"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Source+Code+Pro"),
        StylesheetUtils.createCssLink("prettify/prettify.css"),
        JavaScriptUtils.createJavaScriptLink("prettify/prettify.js")
    );
  }

  private static Element getBody(Option<String> title, Option<String> subtitle, Node[] content) {
    Element.Builder contentBuilder = new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "container");
    if (title.isDefined())
      contentBuilder.addChild(new Element(Tag.H2, TextNode.escapedText(title.getOrThrow())));
    if (subtitle.isDefined())
      contentBuilder.addChild(new Element(Tag.H3, TextNode.escapedText(subtitle.getOrThrow())));
    contentBuilder.addChild(new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "content")
        .addChildren(content)
        .build());

    return new Element.Builder(Tag.BODY)
        .setAttribute(Attribute.ONLOAD, "prettyPrint()")
        .addChild(new Element(Tag.H1,
          new Element.Builder(Tag.A)
              .setAttribute(Attribute.HREF, Config.getBaseUrl())
              .addChild(TextNode.escapedText("Daniel's Blog"))
              .build()
        ))
        .addChild(contentBuilder.build())
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
