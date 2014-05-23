package com.lubarov.daniel.data.util;

public final class HashCodeBuilder {
  private int code = 0;

  public HashCodeBuilder() {}

  public HashCodeBuilder append(Object object) {
    code = 31 * code + (object == null ? 0 : object.hashCode());
    return this;
  }

  public int toHashCode() {
    return code;
  }
}
