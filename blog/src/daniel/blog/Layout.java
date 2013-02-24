package daniel.blog;

import daniel.data.collection.Collection;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.web.html.AnchorBuilder;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.HtmlUtils;
import daniel.web.html.JavaScriptUtils;
import daniel.web.html.Node;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.util.UserAgentUtils;

public final class Layout {
  private Layout() {}

  public static Element createDocument(HttpRequest request,
      Option<String> title, Option<String> dateline, Node... content) {
    Collection<String> notifications = Notifications.getAndClearMessages(request);
    return new Element.Builder(Tag.HTML)
        .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setAttribute("xml:lang", "en")
        .addChild(getHead(isSmallScreen(request)))
        .addChild(getBody(title, dateline, notifications, content))
        .build();
  }

  private static Element getHead(boolean smallScreen) {
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
        new TitleBuilder().addRawText("Daniel Lubarov").build(),
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("common.css"),
        StylesheetUtils.createCssLink(smallScreen ? "mobile.css" : "desktop.css"),
        // TODO: Remove any fonts that aren't used.
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Source+Code+Pro:400,600,700"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Lato"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Merriweather:400,700"),
        StylesheetUtils.createCssLink("prettify/prettify.css"),
        JavaScriptUtils.createJavaScriptLink("prettify/prettify.js")
    );
  }

  private static Element getBody(Option<String> title, Option<String> dateline,
      Collection<String> notifications, Node[] content) {
    Collection<Element> notificationElements = notifications.map(new Function<String, Element>() {
      @Override public Element apply(String message) {
        return new ParagraphBuilder()
            .setClass("notice")
            .addEscapedText(message)
            .build();
      }
    });

    Element.Builder contentBuilder = new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "container")
        .addChildren(notificationElements);
    if (title.isDefined())
      contentBuilder.addChild(new Element.Builder(Tag.H2)
          .addEscapedText(title.getOrThrow())
          .build());
    if (dateline.isDefined())
      contentBuilder.addChild(new Element.Builder(Tag.H6).setAttribute(Attribute.CLASS, "dateline")
          .addEscapedText(dateline.getOrThrow())
          .build());
    contentBuilder.addChild(new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "content")
        .addChildren(content)
        .build());

    Element heading = new Element.Builder(Tag.H1)
        .addChild(new AnchorBuilder()
            .setHref(Config.getBaseUrl())
            .addEscapedText("Daniel Lubarov")
            .build())
        .build();

    return new Element.Builder(Tag.BODY)
        .setAttribute(Attribute.ONLOAD, "prettyPrint()")
        .addChild(heading)
        .addChild(contentBuilder.build())
        .addChild(HtmlUtils.getClearDiv())
        .addChild(getFooter())
        .build();
  }

  private static Element getFooter() {
    Element left = new ParagraphBuilder()
        .addRawText("Copyright &#169; 2013 Daniel Lubarov")
        .build();
    Element validatorLink = new AnchorBuilder()
        .setHref("http://validator.w3.org/check?uri=referer")
        .setTarget("_blank")
        .addEscapedText("Valid XHTML5")
        .build();
    Element right = new ParagraphBuilder()
        .setClass("fr")
        .addChild(validatorLink)
        .build();
    return new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "footer")
        .addChild(right)
        .addChild(left)
        .build();
  }

  private static boolean isSmallScreen(HttpRequest request) {
    Option<String> optUserAgent = request.getHeaders().getValues("User-Agent").tryGetOnlyElement();
    return optUserAgent.isDefined() && UserAgentUtils.isMobile(optUserAgent.getOrThrow());
  }
}
