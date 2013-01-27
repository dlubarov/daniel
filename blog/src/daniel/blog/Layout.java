package daniel.blog;

import daniel.data.collection.Collection;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.HtmlUtils;
import daniel.web.html.JavaScriptUtils;
import daniel.web.html.Node;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;

public final class Layout {
  private Layout() {}

  public static Element createDocument(HttpRequest request,
      Option<String> title, Option<String> subtitle, Node... content) {
    Collection<String> notifications = Notifications.getAndClearMessages(request);
    return new Element.Builder(Tag.HTML)
        .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setAttribute("xml:lang", "en")
        .addChild(getHead())
        .addChild(getBody(title, subtitle, notifications, content))
        .build();
  }

  private static Element getHead() {
    Element description = new Element.Builder(Tag.META)
        .setAttribute(Attribute.NAME, "description")
        .setAttribute(Attribute.CONTENT, "Thoughts on programming languages, compilers, graphics and other fun software topics.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setAttribute(Attribute.NAME, "keywords")
        .setAttribute(Attribute.CONTENT, "daniel, lubarov, java, programming, coding, database, languages, compilers, interpreters, graphics, ray tracing, html, css")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setAttribute(Attribute.HREF, Config.getBaseUrl())
        .build();
    return new Element(Tag.HEAD,
        description, keywords, base,
        new Element(Tag.TITLE, TextNode.rawText("Daniel's Blog")),
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("style.css"),
        // TODO: Remove any fonts that aren't used.
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Source+Code+Pro:400,600,700"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Lato"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Merriweather:400,700"),
        StylesheetUtils.createCssLink("prettify/prettify.css"),
        JavaScriptUtils.createJavaScriptLink("prettify/prettify.js")
    );
  }

  private static Element getBody(Option<String> title, Option<String> subtitle,
      Collection<String> notifications, Node[] content) {
    Collection<Element> notificationElements = notifications.map(new Function<String, Element>() {
      @Override public Element apply(String message) {
        return new Element.Builder(Tag.P)
            .setAttribute(Attribute.CLASS, "notice")
            .addChild(TextNode.escapedText(message))
            .build();
      }
    });

    Element.Builder contentBuilder = new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "container")
        .addChildren(notificationElements);
    if (title.isDefined())
      contentBuilder.addChild(new Element(Tag.H2, TextNode.escapedText(title.getOrThrow())));
    if (subtitle.isDefined())
      contentBuilder.addChild(new Element(Tag.H3, TextNode.escapedText(subtitle.getOrThrow())));
    contentBuilder.addChild(new Element.Builder(Tag.DIV).setAttribute(Attribute.ID, "content")
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
    Element left = new Element.Builder(Tag.P)
        .addChild(TextNode.rawText("Copyright &#169; 2013 Daniel Lubarov"))
        .build();
    Element validatorLink = new Element.Builder(Tag.A)
        .setAttribute(Attribute.HREF, "http://validator.w3.org/check?uri=referer")
        .setAttribute(Attribute.TARGET, "_blank")
        .addChild(TextNode.escapedText("Valid XHTML5"))
        .build();
    Element right = new Element.Builder(Tag.P)
        .setAttribute(Attribute.CLASS, "fr")
        .addChild(validatorLink)
        .build();
    return new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "footer")
        .addChild(right)
        .addChild(left)
        .build();
  }
}
