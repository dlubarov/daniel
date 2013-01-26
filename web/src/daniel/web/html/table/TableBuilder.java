package daniel.web.html.table;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashTable;
import daniel.data.util.Check;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.Tag;

public final class TableBuilder {
  private final MutableHashTable<String, String> attributes = MutableHashTable.create();
  private final TableSectionBuilder theadBuilder = new TableSectionBuilder(Tag.THEAD);
  private final TableSectionBuilder tfootBuilder = new TableSectionBuilder(Tag.TFOOT);
  private final TableSectionBuilder tbodyBuilder = new TableSectionBuilder(Tag.TBODY);
  private TableSectionBuilder currentSectionBuilder = null;

  public TableBuilder() {}

  public TableBuilder beginTableHead() {
    Check.that(currentSectionBuilder == null);
    currentSectionBuilder = theadBuilder;
    return this;
  }

  public TableBuilder beginTableFoot() {
    Check.that(currentSectionBuilder == null);
    currentSectionBuilder = tfootBuilder;
    return this;
  }

  public TableBuilder beginTableBody() {
    Check.that(currentSectionBuilder == null);
    currentSectionBuilder = tbodyBuilder;
    return this;
  }

  public TableBuilder endTableHead() {
    Check.notNull(currentSectionBuilder);
    currentSectionBuilder = null;
    return this;
  }

  public TableBuilder endTableFoot() {
    Check.notNull(currentSectionBuilder);
    currentSectionBuilder = null;
    return this;
  }

  public TableBuilder endTableBody() {
    Check.notNull(currentSectionBuilder);
    currentSectionBuilder = null;
    return this;
  }

  public TableBuilder setTableAttribute(String attribute, String value) {
    attributes.put(attribute, value);
    return this;
  }

  public TableBuilder setTableAttribute(Attribute attribute, String value) {
    return setTableAttribute(attribute.toString(), value);
  }

  public TableBuilder beginRow() {
    currentSectionBuilder.beginRow();
    return this;
  }

  public TableBuilder endRow() {
    currentSectionBuilder.endRow();
    return this;
  }

  public TableBuilder addNormalEntry(Node... contents) {
    currentSectionBuilder.addNormalEntry(contents);
    return this;
  }

  public TableBuilder addHeaderEntry(Node... contents) {
    currentSectionBuilder.addHeaderEntry(contents);
    return this;
  }

  public Element build() {
    Check.that(currentSectionBuilder == null);
    Element.Builder elementBuilder = new Element.Builder(Tag.TABLE);
    for (KeyValuePair<String, String> attribute : attributes)
      elementBuilder.setAttribute(attribute.getKey(), attribute.getValue());
    return elementBuilder
        .addChild(theadBuilder.build())
        .addChild(tfootBuilder.build())
        .addChild(tbodyBuilder.build())
        .build();
  }
}
