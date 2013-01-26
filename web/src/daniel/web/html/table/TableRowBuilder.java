package daniel.web.html.table;

import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.Tag;

final class TableRowBuilder {
  private final MutableStack<Element> entries = DynamicArray.create();

  public TableRowBuilder() {}

  public void addNormalEntry(Node... contents) {
    addEntry(Tag.TD, contents);
  }

  public void addHeaderEntry(Node... contents) {
    addEntry(Tag.TH, contents);
  }

  private void addEntry(Tag tag, Node... contents) {
    entries.pushBack(new Element(tag, contents));
  }

  public Element build() {
    return new Element(Tag.TR, entries);
  }
}
