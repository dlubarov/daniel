package daniel.blog;

import daniel.data.collection.Collection;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.unit.Instant;
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
import daniel.web.http.DateUtils;
import daniel.web.http.HttpRequest;
import daniel.web.util.UserAgentUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class Layout {
  private static final DateFormat datelineFormat = new SimpleDateFormat("MMMMM d, yyyy");

  private Layout() {}

  public static Element createDocument(HttpRequest request,
      Option<String> title, Option<Instant> dateline, Node... content) {
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
        .setAttribute(Attribute.CONTENT,
            "Thoughts on programming languages, compilers, graphics, databases and other fun software topics.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setAttribute(Attribute.NAME, "keywords")
        .setAttribute(Attribute.CONTENT,
            "daniel, lubarov, java, programming, coding, database, languages, compilers, interpreters, graphics, ray tracing, html, css")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setAttribute(Attribute.HREF, Config.getBaseUrl())
        .build();
    return new Element(Tag.HEAD,
        description, keywords, base,
        new TitleBuilder().addRawText("Software and Stuff, a blog by Daniel").build(),
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("common.css"),
        StylesheetUtils.createCssLink(smallScreen ? "mobile.css" : "desktop.css"),
        // TODO: Remove any fonts that aren't used.
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Source+Code+Pro:400,600,700"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Lato"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Merriweather:400,700"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Cutive+Mono"),
        StylesheetUtils.createCssLink("prettify/prettify.css"),
        JavaScriptUtils.createJavaScriptLink("prettify/prettify.js")
    );
  }

  private static Element getBody(Option<String> title, Option<Instant> dateline,
      Collection<String> notifications, Node[] content) {
    Collection<Element> notificationElements = notifications.map(new Function<String, Element>() {
      @Override public Element apply(String message) {
        return new ParagraphBuilder()
            .setClass("notice")
            .addEscapedText(message)
            .build();
      }
    });

    Element.Builder contentBuilder = new Element.Builder(Tag.ARTICLE)
        .setAttribute(Attribute.ID, "container")
        .addChildren(notificationElements);
    Option<Element> optHeader = getHeader(title, dateline);
    if (optHeader.isDefined())
      contentBuilder.addChild(optHeader.getOrThrow());
    contentBuilder.addChild(new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "content")
        .addChildren(content)
        .build());

    Element heading1 = new Element.Builder(Tag.H1)
        .setAttribute(Attribute.ID, "logo")
        .addChild(new AnchorBuilder()
            .setHref(Config.getBaseUrl())
            .addEscapedText("Software & Stuff")
            .build())
        .build();

    Element heading2 = new Element.Builder(Tag.H2)
        .setAttribute(Attribute.ID, "logo2")
        .addEscapedText("a blog by Daniel Lubarov")
        .build();

    return new Element.Builder(Tag.BODY)
        .setAttribute(Attribute.ONLOAD, "prettyPrint()")
        .addChild(heading1)
        .addChild(heading2)
        .addChild(contentBuilder.build())
        .addChild(HtmlUtils.getClearDiv())
        .addChild(getFooter())
        .build();
  }

  private static Option<Element> getHeader(Option<String> title, Option<Instant> dateline) {
    if (title.isEmpty() && dateline.isEmpty())
      return Option.none();
    Element.Builder headerBuilder = new Element.Builder(Tag.HGROUP);
    if (title.isDefined())
      headerBuilder.addChild(new Element.Builder(Tag.H1)
          .setAttribute(Attribute.CLASS, "title")
          .addEscapedText(title.getOrThrow())
          .build());
    if (dateline.isDefined()) {
      Element time = new Element.Builder(Tag.TIME)
          .setAttribute(Attribute.DATETIME, DateUtils.formatIso8601(dateline.getOrThrow()))
          .addEscapedText(datelineFormat.format(dateline.getOrThrow().toDate()))
          .build();
      headerBuilder.addChild(new Element.Builder(Tag.H6)
          .setAttribute(Attribute.CLASS, "dateline")
          .addChild(time)
          .build());
    }
    return Option.some(headerBuilder.build());
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
    return new Element.Builder(Tag.FOOTER)
        .addChild(right)
        .addChild(left)
        .build();
  }

  private static boolean isSmallScreen(HttpRequest request) {
    Option<String> optUserAgent = request.getHeaders().getValues("User-Agent").tryGetOnlyElement();
    return optUserAgent.isDefined() && UserAgentUtils.isMobile(optUserAgent.getOrThrow());
  }
}
