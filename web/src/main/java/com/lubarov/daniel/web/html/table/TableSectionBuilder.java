package com.lubarov.daniel.web.html.table;

import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;
import com.lubarov.daniel.data.util.Check;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.Tag;

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
