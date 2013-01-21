package daniel.web.html;

import daniel.data.dictionary.ImmutableDictionary;
import daniel.data.dictionary.ImmutableHashTable;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.MutableHashTable;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.data.util.Check;

public final class Element implements Node {
  public static class Builder {
    private final Tag tag;
    private final MutableStack<Node> children = DynamicArray.create();
    private final MutableHashTable<Attribute, String> attributes = MutableHashTable.create();

    public Builder(Tag tag) {
      this.tag = Check.notNull(tag);
    }

    public Builder addChild(Node child) {
      children.pushBack(child);
      return this;
    }

    public Builder addChildren(Node... children) {
      for (Node child : children)
        addChild(child);
      return this;
    }

    public Builder setAttribute(Attribute attribute, String value) {
      attributes.put(attribute, value);
      return this;
    }

    public Builder setEscapedAttribtue(Attribute attribute, String value) {
      return setAttribute(attribute, EscapeUtils.htmlEncode(value));
    }

    public Element build() {
      return new Element(this);
    }
  }

  private final Tag tag;
  private final ImmutableSequence<Node> children;
  private final ImmutableDictionary<Attribute, String> attributes;

  public Element(Tag tag) {
    this.tag = tag;
    this.children = ImmutableArray.create();
    this.attributes = ImmutableHashTable.create();
  }

  public Element(Tag tag, Node... children) {
    this.tag = tag;
    this.children = ImmutableArray.create(children);
    this.attributes = ImmutableHashTable.create();
  }

  public Element(Tag tag, Iterable<? extends Node> children) {
    this.tag = tag;
    this.children = ImmutableArray.copyOf(children);
    this.attributes = ImmutableHashTable.create();
  }

  private Element(Builder builder) {
    this.tag = builder.tag;
    this.children = builder.children.toImmutable();
    this.attributes = builder.attributes.toImmutable();
  }

  @Override
  public String toString() {
    //if (children.isEmpty() && tag != Tag.TEXTAREA)
    //  return String.format("<%s%s />", tag, getAttributes());

    StringBuilder sb = new StringBuilder();
    sb.append(String.format("<%s%s>", tag, getAttributes()));
    for (Node child : children)
      sb.append(child);
    sb.append(String.format("</%s>", tag));
    return sb.toString();
  }

  private String getAttributes() {
    StringBuilder sb = new StringBuilder();
    for (KeyValuePair<Attribute, String> attribute : attributes)
      sb.append(String.format(" %s=\"%s\"", attribute.getKey(), attribute.getValue()));
    return sb.toString();
  }
}
