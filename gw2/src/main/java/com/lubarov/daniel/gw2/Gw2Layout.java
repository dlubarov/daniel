package com.lubarov.daniel.gw2;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.html.AnchorBuilder;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.JavaScriptUtils;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.StylesheetUtils;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.html.TitleBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.util.UserAgentUtils;

public class Gw2Layout {
  private Gw2Layout() {}

  public static Element createDocument(HttpRequest request, Option<String> title,
      Collection<String> scripts, Node... content) {
    return new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(getHead(isSmallScreen(request), scripts))
        .addChild(getBody(title, content))
        .build();
  }

  private static Element getHead(boolean smallScreen, Collection<String> scripts) {
    Element description = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "description")
        .setRawAttribute(Attribute.CONTENT, "A collection of tools for Guild Wars 2 players.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "keywords")
        .setRawAttribute(Attribute.CONTENT, "gw2, guild wars 2, tools, calculators, stats, builds")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setRawAttribute(Attribute.HREF, Gw2Config.getBaseUrl())
        .build();
    Element.Builder headBuilder = new Element.Builder(Tag.HEAD)
        .addChildren(description, keywords, base)
        .addChild(new TitleBuilder().addRawText("Guild Wars 2 Stuff").build())
        .addChildren(
            StylesheetUtils.createCssLink("common.css")
//            StylesheetUtils.createCssLink(smallScreen ? "mobile.css" : "desktop.css")
        );
    for (String script : scripts)
      headBuilder.addChild(JavaScriptUtils.createJavaScriptLink(script));
    return headBuilder.build();
  }

  private static Element getBody(Option<String> title, Node[] content) {
    Element logo = new Element.Builder(Tag.H1)
        .setRawAttribute(Attribute.ID, "logo")
        .addChild(new AnchorBuilder()
            .setHref(Gw2Config.getBaseUrl())
            .addEscapedText("Guild Wars 2 Stuff")
            .build())
        .build();

    Element.Builder bodyBuilder = new Element.Builder(Tag.BODY)
        .addChild(logo);
    if (title.isDefined())
      bodyBuilder.addChild(new Element.Builder(Tag.H2)
          .setRawAttribute(Attribute.ID, "title")
          .addEscapedText(title.getOrThrow())
          .build());
    Element container = new Element.Builder(Tag.DIV)
        .setEscapedAttribtue(Attribute.ID, "content")
        .addChildren(content)
        .build();
    return bodyBuilder.addChild(container).build();
  }

  private static boolean isSmallScreen(HttpRequest request) {
    Option<String> optUserAgent = request.getHeaders().getValues("User-Agent").tryGetOnlyElement();
    return optUserAgent.isDefined() && UserAgentUtils.isMobile(optUserAgent.getOrThrow());
  }
}
