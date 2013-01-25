package daniel.parsing;

import daniel.data.option.Option;
import daniel.data.util.ArrayUtils;

public abstract class Parser<A> {
  public abstract Option<ParseResult<A>> tryParse(byte[] data, int p);

  protected final int skipChars(byte[] data, int p, char... charsToSkip) {
    while (p < data.length && ArrayUtils.contains((char) data[p], charsToSkip))
      ++p;
    return p;
  }

  /**
   * Skip all whitespace.
   */
  protected final int skipWS(byte[] data, int p) {
    while (p < data.length && Character.isWhitespace(data[p]))
      ++p;
    return p;
  }

  /**
   * Skip linear whitespace, i.e., regular spaces and tabs.
   */
  protected final int skipLWS(byte[] data, int p) {
    while (p < data.length && (data[p] == ' ' || data[p] == '\t'))
      ++p;
    return p;
  }
}
