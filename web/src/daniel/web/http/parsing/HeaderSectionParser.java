package daniel.web.http.parsing;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.parsing.DelimitedRepetitionParser;
import daniel.parsing.LiteralParser;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;
import java.nio.charset.StandardCharsets;

public final class HeaderSectionParser extends Parser<Sequence<KeyValuePair<String, String>>> {
  public static final HeaderSectionParser singleton = new HeaderSectionParser();

  private static final Parser<Sequence<KeyValuePair<String, String>>> proxy =
      DelimitedRepetitionParser.repeatArbitrarily(HeaderParser.singleton,
          new LiteralParser("\r\n", StandardCharsets.US_ASCII));

  private HeaderSectionParser() {}

  @Override
  public Option<ParseResult<Sequence<KeyValuePair<String, String>>>> tryParse(byte[] data, int p) {
    return proxy.tryParse(data, p);
  }
}
