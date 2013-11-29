package daniel.web.html;

public final class AnchorBuilder {
  private final Element.Builder elementBuilder;

  public AnchorBuilder() {
    elementBuilder = new Element.Builder(Tag.A);
  }

  public AnchorBuilder addChild(Node child) {
    elementBuilder.addChild(child);
    return this;
  }

  public AnchorBuilder addRawText(String text) {
    elementBuilder.addRawText(text);
    return this;
  }

  public AnchorBuilder addEscapedText(String text) {
    elementBuilder.addEscapedText(text);
    return this;
  }

  public AnchorBuilder setHref(String href) {
    elementBuilder.setRawAttribute(Attribute.HREF, href);
    return this;
  }

  public AnchorBuilder setTitle(String href) {
    elementBuilder.setRawAttribute(Attribute.HREF, href);
    return this;
  }

  public AnchorBuilder setClass(String clazz) {
    elementBuilder.setRawAttribute(Attribute.CLASS, clazz);
    return this;
  }

  public AnchorBuilder setStyle(String style) {
    elementBuilder.setRawAttribute(Attribute.STYLE, style);
    return this;
  }

  public AnchorBuilder setTarget(String target) {
    elementBuilder.setRawAttribute(Attribute.TARGET, target);
    return this;
  }

  public Element build() {
    return elementBuilder.build();
  }
}
