package com.lubarov.daniel.web.html;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.ImmutableSequence;
import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;
import com.lubarov.daniel.data.table.sequential.ImmutableArrayTable;
import com.lubarov.daniel.data.table.sequential.ImmutableSequentialTable;
import com.lubarov.daniel.data.util.Check;

public final class Element implements Node {
  public static final class Builder {
    private final Tag tag;
    private final MutableStack<Node> children = DynamicArray.create();
    private final MutableStack<KeyValuePair<String, String>> attributes = DynamicArray.create();

    public Builder(Tag tag) {
      this.tag = Check.notNull(tag);
    }

    public Builder addChild(Node child) {
      children.pushBack(child);
      return this;
    }

    public Builder addRawText(String text) {
      return addChild(TextNode.rawText(text));
    }

    public Builder addEscapedText(String text) {
      return addChild(TextNode.escapedText(text));
    }

    public Builder addChildren(Node... children) {
      for (Node child : children)
        addChild(child);
      return this;
    }

    public Builder addChildren(Iterable<? extends Node> children) {
      for (Node child : children)
        addChild(child);
      return this;
    }

    public Builder setRawAttribute(String attribute, String value) {
      attributes.pushBack(new KeyValuePair<>(attribute, value));
      return this;
    }

    public Builder setRawAttribute(Attribute attribute, String value) {
      return setRawAttribute(attribute.toString(), value);
    }

    public Builder setEscapedAttribtue(String attribute, String value) {
      return setRawAttribute(attribute, EscapeUtils.htmlEncode(value));
    }

    public Builder setEscapedAttribtue(Attribute attribute, String value) {
      return setRawAttribute(attribute, EscapeUtils.htmlEncode(value));
    }

    public Element build() {
      return new Element(this);
    }
  }

  private final Tag tag;
  private final ImmutableSequence<Node> children;
  private final ImmutableSequentialTable<String, String> attributes;

  public Element(Tag tag) {
    this.tag = tag;
    this.children = ImmutableArray.create();
    this.attributes = ImmutableArrayTable.create();
  }

  public Element(Tag tag, Node... children) {
    this.tag = tag;
    this.children = ImmutableArray.create(children);
    this.attributes = ImmutableArrayTable.create();
  }

  public Element(Tag tag, Iterable<? extends Node> children) {
    this.tag = tag;
    this.children = ImmutableArray.copyOf(children);
    this.attributes = ImmutableArrayTable.create();
  }

  private Element(Builder builder) {
    this.tag = builder.tag;
    this.children = builder.children.toImmutable();
    this.attributes = ImmutableArrayTable.copyOf(builder.attributes);
  }

  @Override
  public String toString() {
    if (children.isEmpty() && tag != Tag.TEXTAREA)
      return String.format("<%s%s />", tag, getAttributes());

    StringBuilder sb = new StringBuilder();
    sb.append(String.format("<%s%s>", tag, getAttributes()));
    for (Node child : children)
      sb.append(child);
    sb.append(String.format("</%s>", tag));
    return sb.toString();
  }

  private String getAttributes() {
    StringBuilder sb = new StringBuilder();
    for (KeyValuePair<String, String> attribute : attributes)
      sb.append(String.format(" %s=\"%s\"", attribute.getKey(), attribute.getValue()));
    return sb.toString();
  }
}
