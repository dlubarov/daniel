package com.lubarov.daniel.data.util;

import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;

public final class ToStringBuilder {
  private final Object subject;
  private final MutableStack<Object> fields;

  public ToStringBuilder(Object subject) {
    this.subject = subject;
    this.fields = DynamicArray.create();
  }

  public ToStringBuilder append(Object value) {
    fields.pushBack(value);
    return this;
  }

  public ToStringBuilder append(String fieldName, Object fieldValue) {
    append(String.format("%s=%s", fieldName, fieldValue));
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(subject.getClass().getSimpleName());
    sb.append('[');
    boolean first = true;
    for (Object field : fields) {
      if (first)
        first = false;
      else
        sb.append(", ");
      sb.append(field);
    }
    sb.append(']');
    return sb.toString();
  }
}
