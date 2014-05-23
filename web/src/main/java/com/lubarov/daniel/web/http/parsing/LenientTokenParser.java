package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.set.ImmutableHashSet;
import com.lubarov.daniel.data.set.ImmutableSet;
import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.parsing.Parser;

public final class LenientTokenParser extends Parser<String> {
  public static final LenientTokenParser singleton = new LenientTokenParser();

  private static final ImmutableSet<Character> separators = ImmutableHashSet.create(
      '<', '>', '@', ',', ';', '\\', '\'', '{', '}', ' ', '\t');

  private LenientTokenParser() {}

  @Override
  public Option<ParseResult<String>> tryParse(byte[] data, int p) {
    StringBuilder sb = new StringBuilder();
    while (p < data.length && isTokenChar((char) data[p]))
      sb.append((char) data[p++]);

    if (sb.length() == 0)
      return Option.none();
    return Option.some(new ParseResult<>(sb.toString(), p));
  }

  private static boolean isTokenChar(char c) {
    return c > 31 && c < 127 && !separators.contains(c);
  }
}
