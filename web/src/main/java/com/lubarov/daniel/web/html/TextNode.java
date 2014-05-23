package com.lubarov.daniel.web.html;

public final class TextNode implements Node {
  private final String text;

  private TextNode(String text) {
    this.text = text;
  }

  public static TextNode rawText(String text) {
    return new TextNode(text);
  }

  public static TextNode escapedText(String text) {
    return new TextNode(EscapeUtils.htmlEncode(text));
  }

  @Override
  public String toString() {
    return text;
  }
}
