package daniel.web.http.parsing;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.option.Option;
import daniel.data.util.ArrayUtils;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Parses a key/value pair in a cookie header, for example "$Version=1".
 */
public class CookieKvpHeader extends Parser<KeyValuePair<String, String>> {
  public static final CookieKvpHeader singleton = new CookieKvpHeader();

  private CookieKvpHeader() {}

  @Override
  public Option<ParseResult<KeyValuePair<String, String>>> tryParse(byte[] data, int p) {
    int pEquals = ArrayUtils.firstIndexOf((byte) '=', data, p).getOrDefault(-1);
    if (pEquals <= 0)
      return Option.none();

    String cookieName = new String(data, p, pEquals);
    p = pEquals + 1;

    ParseResult<String> resValue = TokenOrQuotedStringParser.singleton.tryParse(data, p)
        .getOrThrow("No cookie value found.");
    p = resValue.getRem();
    String value;
    try {
      value = URLDecoder.decode(resValue.getValue(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("wtf?");
    }

    KeyValuePair<String, String> result = new KeyValuePair<>(cookieName, value);
    return Option.some(new ParseResult<>(result, p));
  }
}
