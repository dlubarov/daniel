package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.parsing.Parser;

public final class TextParser extends Parser<String> {
  public static final TextParser singleton = new TextParser();

  private TextParser() {}

  @Override
  public Option<ParseResult<String>> tryParse(byte[] data, int p) {
    StringBuilder sb = new StringBuilder();
    while (p < data.length && isTextChar((char) data[p]))
      sb.append((char) data[p++]);

    if (sb.length() == 0)
      return Option.none();
    return Option.some(new ParseResult<>(sb.toString(), p));
  }

  private static boolean isTextChar(char c) {
    return (c > 31 || c == '\t') && c != 127;
  }
}
