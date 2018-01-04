package com.lubarov.daniel.wedding;

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
          new KeyValuePair<>("Photos", "/photos"),
          new KeyValuePair<>("Venue", "/venue"),
          new KeyValuePair<>("Schedule", "/schedule"),
          new KeyValuePair<>("RSVP", "/rsvp")));

  public static Element createDocument(Option<String> pageTitle, Node... content) {
    return new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(getHead(pageTitle))
        .addChild(getBody(content))
        .build();
  }

  private static Element getHead(Option<String> pageTitle) {
    Element description = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "description")
        .setRawAttribute(Attribute.CONTENT, "The wedding of Daniel Lubarov and Vi Dang")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "keywords")
        .setRawAttribute(Attribute.CONTENT, "wedding, daniel lubarov, vi dang")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setRawAttribute(Attribute.HREF, WeddingConfig.getBaseUrl())
        .build();
    Element fontStyles = new Element.Builder(Tag.LINK)
        .setRawAttribute(Attribute.HREF, "https://fonts.googleapis.com/css?family=Alex+Brush|Great+Vibes|Open+Sans|Source+Sans+Pro|Tangerine&amp;subset=vietnamese")
        .setRawAttribute(Attribute.REL, "stylesheet")
        .build();

    String title = pageTitle.isDefined()
        ? pageTitle.getOrThrow() + " | Daniel & Vi Wedding"
        : "Daniel & Vi Wedding";
    Element titleElement = new TitleBuilder().addEscapedText(title).build();

    Element.Builder headBuilder = new Element.Builder(Tag.HEAD)
        .addChildren(description, keywords, base)
        .addChild(titleElement)
        .addChild(StylesheetUtils.createCssLink("common.css"))
        .addChild(fontStyles);
    return headBuilder.build();
  }

  private static Element getBody(Node[] content) {
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

    Element logo = new Element.Builder(Tag.H1)
        .setRawAttribute(Attribute.ID, "logo")
        .addEscapedText("Daniel Lubarov & Vi Dang")
        .build();

    Element date = new Element.Builder(Tag.TIME)
        .setRawAttribute(Attribute.ID, "date")
        .addEscapedText("June 3, 2018")
        .build();

    Element.Builder bodyBuilder = new Element.Builder(Tag.BODY)
        .addChild(navigation)
        .addChild(logo)
        .addChild(date);
    if (content.length > 0) {
      Element container = new Element.Builder(Tag.DIV)
          .setEscapedAttribtue(Attribute.ID, "content")
          .addChildren(content)
          .build();
      bodyBuilder.addChild(container);
    }
    return bodyBuilder.build();
  }
}
