package com.lubarov.daniel.web.html;

public final class InputBuilder {
  private final Element.Builder elementBuilder;

  public InputBuilder() {
    elementBuilder = new Element.Builder(Tag.INPUT);
  }

  public InputBuilder setId(String id) {
    elementBuilder.setRawAttribute(Attribute.ID, id);
    return this;
  }

  public InputBuilder setClass(String clazz) {
    elementBuilder.setRawAttribute(Attribute.CLASS, clazz);
    return this;
  }

  public InputBuilder setStyle(String style) {
    elementBuilder.setRawAttribute(Attribute.STYLE, style);
    return this;
  }

  public InputBuilder setType(String type) {
    elementBuilder.setRawAttribute(Attribute.TYPE, type);
    return this;
  }

  public InputBuilder setName(String name) {
    elementBuilder.setRawAttribute(Attribute.NAME, name);
    return this;
  }

  public InputBuilder setPlaceholder(String placeholder) {
    elementBuilder.setRawAttribute(Attribute.PLACEHOLDER, placeholder);
    return this;
  }

  public InputBuilder setValue(String value) {
    elementBuilder.setRawAttribute(Attribute.VALUE, value);
    return this;
  }

  public InputBuilder setOnChange(String javascript) {
    elementBuilder.setRawAttribute("onchange", javascript);
    return this;
  }

  public InputBuilder setOnKeyDown(String javascript) {
    elementBuilder.setRawAttribute("onkeydown", javascript);
    return this;
  }

  public Element build() {
    return elementBuilder.build();
  }
}
