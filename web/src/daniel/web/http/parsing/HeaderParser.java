package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.parsing.DelimitedRepetitionParser;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;
import daniel.web.http.HttpHeader;

public final class HeaderParser extends Parser<HttpHeader> {
  public static final HeaderParser singleton = new HeaderParser();

  private HeaderParser() {}

  @Override
  public Option<ParseResult<HttpHeader>> tryParse(byte[] data, int p) {
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

    HttpHeader header = new HttpHeader(resName.getValue(), content);
    return Option.some(new ParseResult<>(header, p));
  }
}
