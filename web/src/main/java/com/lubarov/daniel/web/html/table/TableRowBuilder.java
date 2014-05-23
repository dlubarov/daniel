package com.lubarov.daniel.web.html.table;

import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.Tag;

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
