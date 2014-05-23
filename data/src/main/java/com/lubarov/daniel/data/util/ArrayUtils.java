package com.lubarov.daniel.data.util;

import com.lubarov.daniel.data.option.Option;

public final class ArrayUtils {
  public static final Object[] EMPTY_ARRAY = new Object[0];

  private ArrayUtils() {}

  public static <A> boolean contains(A needle, A[] haystack) {
    for (A element : haystack)
      if (element.equals(needle))
        return true;
    return false;
  }

  public static <A> boolean contains(char needle, char[] haystack) {
    for (char element : haystack)
      if (element == needle)
        return true;
    return false;
  }

  public static <A> Option<Integer> firstIndexOf(A needle, A[] haystack, int start) {
    for (int i = start; i < haystack.length; ++i)
      if (haystack[i].equals(needle))
        return Option.some(i);
    return Option.none();
  }

  public static <A> Option<Integer> firstIndexOf(A needle, A[] haystack) {
    return firstIndexOf(needle, haystack, 0);
  }

  public static Option<Integer> firstIndexOf(char needle, char[] haystack, int start) {
    for (int i = start; i < haystack.length; ++i)
      if (haystack[i] == needle)
        return Option.some(i);
    return Option.none();
  }

  public static Option<Integer> firstIndexOf(char needle, char[] haystack) {
    return firstIndexOf(needle, haystack, 0);
  }

  public static Option<Integer> firstIndexOf(byte needle, byte[] haystack, int start) {
    for (int i = start; i < haystack.length; ++i)
      if (haystack[i] == needle)
        return Option.some(i);
    return Option.none();
  }

  public static Option<Integer> firstIndexOf(byte needle, byte[] haystack) {
    return firstIndexOf(needle, haystack, 0);
  }

  public static Option<Integer> firstIndexOf(byte[] needle, byte[] haystack, int start) {
    search:
    for (int i = start; i + needle.length <= haystack.length; ++i) {
      for (int j = 0; j < needle.length; ++j)
        if (haystack[i + j] != needle[j])
          continue search;
      return Option.some(i);
    }
    return Option.none();
  }

  public static Option<Integer> firstIndexOf(byte[] needle, byte[] haystack) {
    return firstIndexOf(needle, haystack, 0);
  }
}
