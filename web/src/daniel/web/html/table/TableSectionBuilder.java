package daniel.web.html.table;

import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.data.util.Check;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.Tag;

final class TableSectionBuilder {
  private final Tag tag;
  private final MutableStack<Element> completedRows = DynamicArray.create();
  private TableRowBuilder currentRowBuilder = null;

  public TableSectionBuilder(Tag tag) {
    this.tag = tag;
  }

  public void beginRow() {
    Check.that(currentRowBuilder == null);
    currentRowBuilder = new TableRowBuilder();
  }

  public void endRow() {
    completedRows.pushBack(currentRowBuilder.build());
    currentRowBuilder = null;
  }

  public void addNormalEntry(Node... contents) {
    currentRowBuilder.addNormalEntry(contents);
  }

  public void addHeaderEntry(Node... contents) {
    currentRowBuilder.addHeaderEntry(contents);
  }

  public Element build() {
    Check.that(currentRowBuilder == null);
    Element.Builder elementBuilder = new Element.Builder(tag);
    elementBuilder.addChildren(completedRows);
    return elementBuilder.build();
  }
}
