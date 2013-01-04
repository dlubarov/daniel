package daniel.web.html;

import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public final class TableBuilder {
  private final MutableStack<MutableStack<ImmutableSequence<Element>>> entries;

  public TableBuilder() {
    this.entries = DynamicArray.create();
    newRow();
  }

  public TableBuilder newRow() {
    entries.pushBack(DynamicArray.<ImmutableSequence<Element>>create());
    return this;
  }

  public TableBuilder addColumn(Element... contents) {
    entries.getBack().pushBack(ImmutableArray.create(contents));
    return this;
  }

  public Element build() {
    Element.Builder tableBuilder = new Element.Builder(Tag.TABLE);
    for (MutableStack<ImmutableSequence<Element>> row : entries) {
      Element.Builder rowBuilder = new Element.Builder(Tag.TR);
      for (ImmutableSequence<Element> entry : row)
        rowBuilder.addChild(new Element(Tag.TD, entry));
      tableBuilder.addChild(rowBuilder.build());
    }
    return tableBuilder.build();
  }
}
