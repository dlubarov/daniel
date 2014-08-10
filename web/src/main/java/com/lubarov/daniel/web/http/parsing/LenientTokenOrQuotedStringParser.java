package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.parsing.DisjunctiveParser;
import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.parsing.Parser;

public final class LenientTokenOrQuotedStringParser extends Parser<String> {
  public static final LenientTokenOrQuotedStringParser
      singleton = new LenientTokenOrQuotedStringParser();

  private static final DisjunctiveParser<String> proxy =
      new DisjunctiveParser<String>(QuotedStringParser.singleton, LenientTokenParser.singleton);

  private LenientTokenOrQuotedStringParser() {}

  @Override
  public Option<ParseResult<String>> tryParse(byte[] data, int p) {
    return proxy.tryParse(data, p);
  }
}
