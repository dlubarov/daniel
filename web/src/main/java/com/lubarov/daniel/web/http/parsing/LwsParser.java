package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.parsing.Parser;

/**
 * A parser of "linear white space" as it is defined in the HTTP spec:
 *
 * LWS = [CRLF] 1*( SP | HT )
 */
public final class LwsParser extends Parser<String> {
  public static final LwsParser singleton = new LwsParser();

  private LwsParser() {}

  @Override
  public Option<ParseResult<String>> tryParse(byte[] data, int p) {
    StringBuilder sb = new StringBuilder();
    if (data[p] == '\r' && data[p + 1] == '\n') {
      sb.append("\r\n");
      p += 2;
    }

    int actuallyLinearSpaces = 0;
    while (p < data.length && (data[p] == ' ' || data[p] == '\t')) {
      sb.append((char) data[p++]);
      ++actuallyLinearSpaces;
    }

    if (actuallyLinearSpaces == 0)
      return Option.none();
    return Option.some(new ParseResult<>(sb.toString(), p));
  }
}
