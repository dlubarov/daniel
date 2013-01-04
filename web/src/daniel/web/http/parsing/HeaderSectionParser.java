package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.parsing.DelimitedRepetitionParser;
import daniel.parsing.LiteralParser;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;
import daniel.web.http.HttpHeader;
import java.nio.charset.StandardCharsets;

public final class HeaderSectionParser extends Parser<Sequence<HttpHeader>> {
  public static final HeaderSectionParser singleton = new HeaderSectionParser();

  private static final Parser<Sequence<HttpHeader>> proxy =
      DelimitedRepetitionParser.repeatArbitrarily(HeaderParser.singleton,
          new LiteralParser("\r\n", StandardCharsets.US_ASCII));

  private HeaderSectionParser() {}

  @Override
  public Option<ParseResult<Sequence<HttpHeader>>> tryParse(byte[] data, int p) {
    return proxy.tryParse(data, p);
  }
}
