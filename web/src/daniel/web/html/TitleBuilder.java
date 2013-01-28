package daniel.web.html;

public final class TitleBuilder {
  private final Element.Builder elementBuilder;

  public TitleBuilder() {
    elementBuilder = new Element.Builder(Tag.TITLE);
  }

  public TitleBuilder addRawText(String text) {
    elementBuilder.addRawText(text);
    return this;
  }

  public TitleBuilder addEscapedText(String text) {
    elementBuilder.addEscapedText(text);
    return this;
  }

  public TitleBuilder setClass(String clazz) {
    elementBuilder.setAttribute(Attribute.CLASS, clazz);
    return this;
  }

  public TitleBuilder setStyle(String style) {
    elementBuilder.setAttribute(Attribute.STYLE, style);
    return this;
  }

  public Element build() {
    return elementBuilder.build();
  }
}
