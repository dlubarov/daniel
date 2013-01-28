package daniel.web.html;

public final class InputBuilder {
  private final Element.Builder elementBuilder;

  public InputBuilder() {
    elementBuilder = new Element.Builder(Tag.INPUT);
  }

  public InputBuilder setClass(String clazz) {
    elementBuilder.setAttribute(Attribute.CLASS, clazz);
    return this;
  }

  public InputBuilder setStyle(String style) {
    elementBuilder.setAttribute(Attribute.STYLE, style);
    return this;
  }

  public InputBuilder setType(String type) {
    elementBuilder.setAttribute(Attribute.TYPE, type);
    return this;
  }

  public InputBuilder setName(String name) {
    elementBuilder.setAttribute(Attribute.NAME, name);
    return this;
  }

  public InputBuilder setValue(String value) {
    elementBuilder.setAttribute(Attribute.VALUE, value);
    return this;
  }

  public Element build() {
    return elementBuilder.build();
  }
}
