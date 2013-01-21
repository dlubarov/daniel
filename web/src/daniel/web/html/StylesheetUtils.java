package daniel.web.html;

public final class StylesheetUtils {
  private StylesheetUtils() {}

  public static Element createCssLink(String href) {
    return new Element.Builder(Tag.LINK)
        .setAttribute(Attribute.REL, "stylesheet")
        .setAttribute(Attribute.TYPE, "text/css")
        .setAttribute(Attribute.HREF, href)
        .build();
  }
}
