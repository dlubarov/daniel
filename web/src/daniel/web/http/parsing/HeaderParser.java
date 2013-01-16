package daniel.web.http.parsing;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.parsing.DelimitedRepetitionParser;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;

public final class HeaderParser extends Parser<KeyValuePair<String, String>> {
  public static final HeaderParser singleton = new HeaderParser();

  private HeaderParser() {}

  @Override
  public Option<ParseResult<KeyValuePair<String, String>>> tryParse(byte[] data, int p) {
    Option<ParseResult<String>> optResName = TokenParser.singleton.tryParse(data, p);
    if (optResName.isEmpty())
      return Option.none();
    ParseResult<String> resName = optResName.getOrThrow();
    p = resName.getRem();

    p = skipWS(data, p);
    if (data[p++] != ':')
      return Option.none();
    p = skipWS(data, p);

    ParseResult<Sequence<String>> resContent = DelimitedRepetitionParser
        .repeatArbitrarily(TextParser.singleton, LwsParser.singleton)
        .tryParse(data, p).getOrThrow();
    p = resContent.getRem();

    // "A recipient MAY replace any linear white space with a single SP
    // before interpreting the field value ..."
    String content = resContent.getValue().join(" ");

    KeyValuePair<String, String> header = new KeyValuePair<>(resName.getValue(), content);
    return Option.some(new ParseResult<>(header, p));
  }
}
