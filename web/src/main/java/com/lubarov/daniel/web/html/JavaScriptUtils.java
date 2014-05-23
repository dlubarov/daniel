package com.lubarov.daniel.web.html;

public final class JavaScriptUtils {
  private JavaScriptUtils() {}

  public static Element createJavaScriptLink(String sourcePath) {
    return new Element.Builder(Tag.SCRIPT)
        .setRawAttribute(Attribute.TYPE, "text/javascript")
        .setRawAttribute(Attribute.SRC, sourcePath)
        .build();
  }
}
