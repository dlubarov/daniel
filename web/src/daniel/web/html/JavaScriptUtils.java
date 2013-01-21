package daniel.web.html;

public final class JavaScriptUtils {
  private JavaScriptUtils() {}

  public static Element createJavaScriptLink(String sourcePath) {
    return new Element.Builder(Tag.SCRIPT)
        .setAttribute(Attribute.TYPE, "text/javascript")
        .setAttribute(Attribute.SRC, sourcePath)
        .build();
  }
}
