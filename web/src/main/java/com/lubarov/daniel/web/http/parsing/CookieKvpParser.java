package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.util.ArrayUtils;
import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.parsing.Parser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Parses a key/value pair in a cookie header, for example "$Version=1".
 */
public final class CookieKvpParser extends Parser<KeyValuePair<String, String>> {
  public static final CookieKvpParser singleton = new CookieKvpParser();

  private CookieKvpParser() {}

  @Override
  public Option<ParseResult<KeyValuePair<String, String>>> tryParse(byte[] data, int p) {
    int pEquals = ArrayUtils.firstIndexOf((byte) '=', data, p).getOrDefault(-1);
    if (pEquals <= 0)
      return Option.none();

    String cookieName = new String(data, p, pEquals - p);
    p = pEquals + 1;

    ParseResult<String> resValue = LenientTokenOrQuotedStringParser.singleton.tryParse(data, p)
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
