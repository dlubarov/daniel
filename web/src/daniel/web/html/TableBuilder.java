package daniel.web.html;

public final class TableBuilder {
  private final Element.Builder tableBuilder;
  private Element.Builder rowBuilder;

  public TableBuilder() {
    this.tableBuilder = new Element.Builder(Tag.TABLE);
  }

  public TableBuilder setAttribute(Attribute attribute, String value) {
    tableBuilder.setAttribute(attribute, value);
    return this;
  }

  public TableBuilder beginRow() {
    rowBuilder = new Element.Builder(Tag.TR);
    return this;
  }

  public TableBuilder endRow() {
    tableBuilder.addChild(rowBuilder.build());
    return this;
  }

  public TableBuilder addEntry(Node... contents) {
    rowBuilder.addChild(new Element(Tag.TD, contents));
    return this;
  }

  public TableBuilder addHeaderEntry(Node... contents) {
    rowBuilder.addChild(new Element(Tag.TH, contents));
    return this;
  }

  public Element build() {
    return tableBuilder.build();
  }
}
