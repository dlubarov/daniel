package com.lubarov.daniel.web.html;

import com.lubarov.daniel.data.sequence.Sequence;

public final class ParagraphBuilder {
  private final Element.Builder elementBuilder;

  public ParagraphBuilder() {
    elementBuilder = new Element.Builder(Tag.P);
  }

  public ParagraphBuilder addChild(Node child) {
    elementBuilder.addChild(child);
    return this;
  }

  public ParagraphBuilder addChildren(Node... children) {
    elementBuilder.addChildren(children);
    return this;
  }

  public ParagraphBuilder addChildren(Iterable<Node> children) {
    elementBuilder.addChildren(children);
    return this;
  }

  public ParagraphBuilder addRawText(String text) {
    elementBuilder.addRawText(text);
    return this;
  }

  public ParagraphBuilder addEscapedText(String text) {
    elementBuilder.addEscapedText(text);
    return this;
  }

  public ParagraphBuilder setId(String id) {
    elementBuilder.setRawAttribute(Attribute.ID, id);
    return this;
  }

  public ParagraphBuilder setClass(String clazz) {
    elementBuilder.setRawAttribute(Attribute.CLASS, clazz);
    return this;
  }

  public ParagraphBuilder setStyle(String style) {
    elementBuilder.setRawAttribute(Attribute.STYLE, style);
    return this;
  }

  public Element build() {
    return elementBuilder.build();
  }
}
