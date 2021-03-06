package com.lubarov.daniel.conniewedding;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.table.sequential.ImmutableArrayTable;
import com.lubarov.daniel.data.table.sequential.ImmutableSequentialTable;
import com.lubarov.daniel.web.html.AnchorBuilder;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.StylesheetUtils;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.html.TitleBuilder;

public class WeddingLayout {
  private WeddingLayout() {}

  private static final ImmutableSequentialTable<String, String> NAVIGATION =
      ImmutableArrayTable.copyOf(ImmutableArray.create(
          new KeyValuePair<>("Home", "/"),
          new KeyValuePair<>("Details", "/details"),
//          new KeyValuePair<>("Photos", "/photos"),
//          new KeyValuePair<>("Gifts", "/gifts"),
          new KeyValuePair<>("RSVP", "/rsvp")));

  public static Element createDocument(Option<String> pageTitle, Node... content) {
    return new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(getHead(pageTitle))
        .addChild(getBody(pageTitle, content))
        .build();
  }

  private static Element getHead(Option<String> pageTitle) {
    Element description = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "description")
        .setRawAttribute(Attribute.CONTENT, "The wedding of Leo Yeung and Connie Li")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "keywords")
        .setRawAttribute(Attribute.CONTENT, "wedding, Leo Yeung, Connie Li")
        .build();
    Element viewport = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "viewport")
        .setRawAttribute(Attribute.CONTENT, "width=device-width, initial-scale=1")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setRawAttribute(Attribute.HREF, WeddingConfig.getBaseUrl())
        .build();
    Element fontStyles = new Element.Builder(Tag.LINK)
        .setRawAttribute(Attribute.HREF, "https://fonts.googleapis.com/css?family=Alex+Brush|Great+Vibes|Open+Sans|Source+Sans+Pro|Tangerine".replace("|", "%7C"))
        .setRawAttribute(Attribute.REL, "stylesheet")
        .build();
    Element jQuery = new Element.Builder(Tag.SCRIPT)
        .setRawAttribute(Attribute.SRC, "https://code.jquery.com/jquery-3.3.1.min.js")
        .build();

    String title = pageTitle.isDefined()
        ? pageTitle.getOrThrow() + " | Leo & Connie Wedding"
        : "Leo & Connie Wedding";
    Element titleElement = new TitleBuilder().addEscapedText(title).build();

    Element.Builder headBuilder = new Element.Builder(Tag.HEAD)
        .addChildren(description, keywords, viewport, base)
        .addChild(titleElement)
        .addChild(StylesheetUtils.createCssLink("common.css"))
        .addChild(fontStyles)
        .addChild(jQuery);
    return headBuilder.build();
  }

  private static Element getBody(Option<String> pageTitle, Node[] content) {
    Element.Builder navigationBuilder = new Element.Builder(Tag.UL)
        .setRawAttribute(Attribute.ID, "navigation");
    for (KeyValuePair<String, String> entry : NAVIGATION) {
      Element link = new AnchorBuilder()
          .setHref(entry.getValue())
          .addEscapedText(entry.getKey())
          .build();
      navigationBuilder.addChild(new Element.Builder(Tag.LI).addChild(link).build());
    }
    Element navigation = navigationBuilder.build();

    Element logoLeo = new Element.Builder(Tag.SPAN)
        .setRawAttribute("style", "display: inline-block;")
        .addEscapedText("Leo Yeung")
        .build();
    Element logoConnie = new Element.Builder(Tag.SPAN)
        .setRawAttribute("style", "display: inline-block;")
        .addEscapedText("Connie Li")
        .build();
    Element logo = new Element.Builder(Tag.H1)
        .setRawAttribute(Attribute.ID, "logo")
        .addChild(logoLeo)
        .addEscapedText(" & ")
        .addChild(logoConnie)
        .build();

    Element date = new Element.Builder(Tag.TIME)
        .setRawAttribute(Attribute.ID, "date")
        .setEscapedAttribtue(Attribute.DATETIME, "2018-07-28T16:00")
        .addEscapedText("July 28, 2018")
        .build();

    Element.Builder bodyBuilder = new Element.Builder(Tag.BODY)
        .addChild(navigation)
        .addChild(logo)
        .addChild(date);
    if (content.length > 0) {
      Element.Builder containerBuilder = new Element.Builder(Tag.DIV)
          .setEscapedAttribtue(Attribute.ID, "content");
      if (pageTitle.isDefined())
        containerBuilder.addChild(new Element.Builder(Tag.H1)
            .setRawAttribute(Attribute.ID, "title")
            .addEscapedText(pageTitle.getOrThrow())
            .build());
      Element container = containerBuilder.addChildren(content).build();
      bodyBuilder.addChild(container);
    }
    return bodyBuilder.build();
  }
}
