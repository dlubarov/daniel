package daniel.parsing;

import daniel.data.option.Option;
import java.nio.charset.Charset;

public final class LiteralParser extends Parser<byte[]> {
  private final byte[] literal;

  public LiteralParser(byte[] literal) {
    this.literal = literal;
  }

  public LiteralParser(String s, Charset charset) {
    this(s.getBytes(charset));
  }

  @Override
  public Option<ParseResult<byte[]>> tryParse(byte[] data, int p) {
    for (int i = 0; i < literal.length; ++i)
      if (data[p + i] != literal[i])
        return Option.none();
    return Option.some(new ParseResult<>(literal, p + literal.length));
  }
}
