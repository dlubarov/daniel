package com.lubarov.daniel.data.util;

public final class EqualsBuilder {
  private boolean allEqual = true;

  public EqualsBuilder append(boolean condition) {
    allEqual &= condition;
    return this;
  }

  public EqualsBuilder append(Object a, Object b) {
    return append(a.equals(b));
  }

  public boolean isEquals() {
    return allEqual;
  }
}
