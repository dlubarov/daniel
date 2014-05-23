package com.lubarov.daniel.data.util;

public final class Check {
  private Check() {}

  public static void that(boolean condition, String format, Object... args) {
    if (!condition)
      throw new RuntimeException(String.format(format, args));
  }

  public static void that(boolean condition) {
    Check.that(condition, "Condition is not satisfied.");
  }

  public static <T> T notNull(T reference, String format, Object... args) {
    Check.that(reference != null, format, args);
    return reference;
  }

  public static <T> T notNull(T reference) {
    return Check.notNull(reference, "Reference is null.");
  }
}
