package com.lubarov.daniel.data.order;

public enum Relation {
  LT, EQ, GT;

  public Relation getOpposite() {
    switch (this) {
      case LT:
        return GT;
      case EQ:
        return EQ;
      case GT:
        return LT;
      default:
        throw new AssertionError();
    }
  }
}
