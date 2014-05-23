package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.parsing.DisjunctiveParser;
import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.parsing.Parser;

public final class TokenOrQuotedStringParser extends Parser<String> {
  public static final TokenOrQuotedStringParser singleton = new TokenOrQuotedStringParser();

  private static final DisjunctiveParser<String> proxy =
      new DisjunctiveParser<>(QuotedStringParser.singleton, TokenParser.singleton);

  private TokenOrQuotedStringParser() {}

  @Override
  public Option<ParseResult<String>> tryParse(byte[] data, int p) {
    return proxy.tryParse(data, p);
  }
}
