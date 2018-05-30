package com.lubarov.daniel.blog;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.html.AnchorBuilder;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.HtmlUtils;
import com.lubarov.daniel.web.html.JavaScriptUtils;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.StylesheetUtils;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.html.TextNode;
import com.lubarov.daniel.web.html.TitleBuilder;
import com.lubarov.daniel.web.http.DateUtils;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.util.UserAgentUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class Layout {
  private static final DateFormat datelineFormat = new SimpleDateFormat("MMMMM d, yyyy");

  private Layout() {}

  public static Element createDocument(HttpRequest request,
      Option<String> title, Option<Instant> dateline, Node... content) {
    Collection<String> notifications = Notifications.getAndClearMessages(request);
    return new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(getHead(isSmallScreen(request)))
        .addChild(getBody(title, dateline, notifications, content))
        .build();
  }

  private static Element getHead(boolean smallScreen) {
    Element description = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "description")
        .setRawAttribute(Attribute.CONTENT,
            "Thoughts on programming languages, compilers, graphics, databases and other fun software topics.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "keywords")
        .setRawAttribute(Attribute.CONTENT,
            "daniel, lubarov, java, programming, coding, database, languages, compilers, interpreters, graphics, ray tracing, html, css")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setRawAttribute(Attribute.HREF, Config.getBaseUrl())
        .build();
    return new Element(Tag.HEAD,
        description, keywords, base,
        new TitleBuilder().addRawText("Daniel Lubarov").build(),
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("common.css"),
        StylesheetUtils.createCssLink(smallScreen ? "mobile.css" : "desktop.css"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Source+Code+Pro:400,600,700"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Lato"),
        StylesheetUtils.createCssLink("http://fonts.googleapis.com/css?family=Poiret+One"),
        StylesheetUtils.createCssLink("prettify/prettify.css"),
        JavaScriptUtils.createJavaScriptLink("prettify/prettify.js"),
        TextNode.rawText(""
            + "<script type=\"text/javascript\">\n"
            + "  var _gaq = _gaq || [];\n"
            + "  _gaq.push(['_setAccount', 'UA-39230019-1']);\n"
            + "  _gaq.push(['_trackPageview']);\n"
            + "  (function() {\n"
            + "    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n"
            + "    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n"
            + "    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n"
            + "  })();\n"
            + "</script>")
    );
  }

  private static Element getBody(Option<String> title, Option<Instant> dateline,
      Collection<String> notifications, Node[] content) {
    Collection<Element> notificationElements = notifications.map(message -> new ParagraphBuilder()
        .setClass("notice")
        .addEscapedText(message)
        .build());

    Element.Builder contentBuilder = new Element.Builder(Tag.ARTICLE)
        .setRawAttribute(Attribute.ID, "container")
        .addChildren(notificationElements);
    Option<Element> optHeader = getHeader(title, dateline);
    if (optHeader.isDefined())
      contentBuilder.addChild(optHeader.getOrThrow());
    contentBuilder.addChild(new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.ID, "content")
        .addChildren(content)
        .build());

    Element heading1 = new Element.Builder(Tag.H1)
        .setRawAttribute(Attribute.ID, "logo")
        .addChild(new AnchorBuilder()
            .setHref(Config.getBaseUrl())
            .addEscapedText("Daniel Lubarov")
            .build())
        .build();

    return new Element.Builder(Tag.BODY)
        .setRawAttribute(Attribute.ONLOAD, "prettyPrint()")
        .addChild(heading1)
            //.addChild(heading2)
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
          .setRawAttribute(Attribute.CLASS, "title")
          .addEscapedText(title.getOrThrow())
          .build());
    if (dateline.isDefined()) {
      Element time = new Element.Builder(Tag.TIME)
          .setRawAttribute(Attribute.DATETIME, DateUtils.formatIso8601(dateline.getOrThrow()))
          .addEscapedText(datelineFormat.format(dateline.getOrThrow().toDate()))
          .build();
      headerBuilder.addChild(new Element.Builder(Tag.H6)
          .setRawAttribute(Attribute.CLASS, "dateline")
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
