package daniel.pokemon;

import daniel.data.option.Option;
import daniel.web.html.AnchorBuilder;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.HtmlUtils;
import daniel.web.html.Node;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.util.UserAgentUtils;

public final class Layout {
  private Layout() {}

  public static Element createDocument(HttpRequest request, Option<String> title, Node... content) {
    return new Element.Builder(Tag.HTML)
        .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setAttribute("xml:lang", "en")
        .addChild(getHead(isSmallScreen(request)))
        .addChild(getBody(title, content))
        .build();
  }

  private static Element getHead(boolean smallScreen) {
    Element description = new Element.Builder(Tag.META)
        .setAttribute(Attribute.NAME, "description")
        .setAttribute(Attribute.CONTENT, "A Pokemon databse and search engine.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setAttribute(Attribute.NAME, "keywords")
        .setAttribute(Attribute.CONTENT, "pokemon, database, search")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setAttribute(Attribute.HREF, Config.getBaseUrl())
        .build();
    return new Element(Tag.HEAD,
        description, keywords, base,
        new TitleBuilder().addRawText("Professor Oak").build(),
        StylesheetUtils.createCssLink("reset.css"),
        StylesheetUtils.createCssLink("common.css"),
        StylesheetUtils.createCssLink(smallScreen ? "mobile.css" : "desktop.css")
    );
  }

  private static Element getBody(Option<String> title, Node[] content) {
    Element.Builder contentBuilder = new Element.Builder(Tag.ARTICLE)
        .setAttribute(Attribute.ID, "container");
    if (title.isDefined())
      contentBuilder.addChild(getHeader(title.getOrThrow()));
    contentBuilder.addChild(new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.ID, "content")
        .addChildren(content)
        .build());

    Element heading1 = new Element.Builder(Tag.H1)
        .setAttribute(Attribute.ID, "logo")
        .addChild(new AnchorBuilder()
            .setHref(Config.getBaseUrl())
            .addEscapedText("Professor Oak")
            .build())
        .build();

    Element heading2 = new Element.Builder(Tag.H2)
        .setAttribute(Attribute.ID, "logo2")
        .addEscapedText("The Pokemon database")
        .build();

    return new Element.Builder(Tag.BODY)
        .addChild(heading1)
        .addChild(heading2)
        .addChild(contentBuilder.build())
        .addChild(HtmlUtils.getClearDiv())
        .addChild(getFooter())
        .build();
  }

  private static Element getHeader(String title) {
    Element.Builder headerBuilder = new Element.Builder(Tag.HGROUP);
    headerBuilder.addChild(new Element.Builder(Tag.H1)
        .setAttribute(Attribute.CLASS, "title")
        .addEscapedText(title)
        .build());
    return headerBuilder.build();
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
