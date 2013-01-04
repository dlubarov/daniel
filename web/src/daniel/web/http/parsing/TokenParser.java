package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.data.set.ImmutableHashSet;
import daniel.data.set.ImmutableSet;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;

public final class TokenParser extends Parser<String> {
  public static final TokenParser singleton = new TokenParser();

  private static final ImmutableSet<Character> separators = ImmutableHashSet.create(
      '(', ')', '<', '>', '@', ',', ';', ':', '\\', '\'',
      '/', '[', ']', '?', '=', '{', '}', ' ', '\t');

  private TokenParser() {}

  @Override
  public Option<ParseResult<String>> tryParse(byte[] data, int p) {
    StringBuilder sb = new StringBuilder();
    while (p < data.length && isTokenChar((char) data[p]))
      sb.append((char) data[p++]);

    if (sb.length() == 0)
      return Option.none();
    return Option.some(new ParseResult<>(sb.toString(), p));
  }

  private static boolean isTokenChar(char c) {
    return c > 31 && c < 127 && !separators.contains(c);
  }
}
