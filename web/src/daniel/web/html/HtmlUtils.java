package daniel.web.html;

public final class HtmlUtils {
  private HtmlUtils() {}

  private static final Element clearDiv = new Element.Builder(Tag.DIV)
      .setAttribute(Attribute.STYLE, "clear: both;")
      .build();

  public static Element getClearDiv() {
    return clearDiv;
  }
}
