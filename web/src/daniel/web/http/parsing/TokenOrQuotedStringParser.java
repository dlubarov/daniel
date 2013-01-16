package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.parsing.DisjunctiveParser;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;

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
