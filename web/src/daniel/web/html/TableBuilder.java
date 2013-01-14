package daniel.web.html;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashTable;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public final class TableBuilder {
  private final MutableHashTable<Attribute, String> attributes = MutableHashTable.create();
  private final MutableStack<MutableStack<ImmutableSequence<Node>>> entries;

  public TableBuilder() {
    this.entries = DynamicArray.create();
  }

  public TableBuilder setAttribute(Attribute attribute, String value) {
    attributes.put(attribute, value);
    return this;
  }

  public TableBuilder beginRow() {
    entries.pushBack(DynamicArray.<ImmutableSequence<Node>>create());
    return this;
  }

  public TableBuilder addColumn(Node... contents) {
    entries.getBack().pushBack(ImmutableArray.create(contents));
    return this;
  }

  public Element build() {
    Element.Builder tableBuilder = new Element.Builder(Tag.TABLE);
    for (KeyValuePair<Attribute, String> attribute : attributes)
      tableBuilder.setAttribute(attribute.getKey(), attribute.getValue());
    for (MutableStack<ImmutableSequence<Node>> row : entries) {
      Element.Builder rowBuilder = new Element.Builder(Tag.TR);
      for (ImmutableSequence<Node> entry : row)
        rowBuilder.addChild(new Element(Tag.TD, entry));
      tableBuilder.addChild(rowBuilder.build());
    }
    return tableBuilder.build();
  }
}
