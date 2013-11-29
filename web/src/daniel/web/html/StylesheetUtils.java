package daniel.web.html;

public final class StylesheetUtils {
  private StylesheetUtils() {}

  public static Element createCssLink(String href) {
    return new Element.Builder(Tag.LINK)
        .setRawAttribute(Attribute.REL, "stylesheet")
        .setRawAttribute(Attribute.TYPE, "text/css")
        .setRawAttribute(Attribute.HREF, href)
        .build();
  }
}
