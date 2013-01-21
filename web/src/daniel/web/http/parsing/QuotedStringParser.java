package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;

public final class QuotedStringParser extends Parser<String> {
  public static final QuotedStringParser singleton = new QuotedStringParser();

  private QuotedStringParser() {}

  @Override
  public Option<ParseResult<String>> tryParse(byte[] data, int p) {
    if (data[p++] != '"')
      return Option.none();
    StringBuilder sb = new StringBuilder();
    while (p < data.length) {
      if (data[p] == '"')
        return Option.some(new ParseResult<>(sb.toString(), p + 1));
      if (data[p] == '\\') {
        sb.append(data[p + 1]);
        p += 2;
      } else {
        sb.append(data[p++]);
      }
    }
    return Option.none();
  }
}
