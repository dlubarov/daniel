package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.parsing.DisjunctiveParser;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;

public final class LenientTokenOrQuotedStringParser extends Parser<String> {
  public static final LenientTokenOrQuotedStringParser
      singleton = new LenientTokenOrQuotedStringParser();

  private static final DisjunctiveParser<String> proxy =
      new DisjunctiveParser<>(QuotedStringParser.singleton, LenientTokenParser.singleton);

  private LenientTokenOrQuotedStringParser() {}

  @Override
  public Option<ParseResult<String>> tryParse(byte[] data, int p) {
    return proxy.tryParse(data, p);
  }
}
