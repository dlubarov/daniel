package daniel.web.http.parsing;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.multidictionary.sequential.ImmutableArrayMultidictionary;
import daniel.data.multidictionary.sequential.SequentialMultidictionary;
import daniel.data.option.Option;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;

/**
 * Parses a cookie header value, for example "$Version=1; Skin=new;".
 */
public final class CookieHeaderParser extends Parser<SequentialMultidictionary<String, String>> {
  public static final CookieHeaderParser singleton = new CookieHeaderParser();

  private CookieHeaderParser() {}

  @Override
  public Option<ParseResult<SequentialMultidictionary<String, String>>> tryParse(
      byte[] data, int p) {
    MutableStack<KeyValuePair<String, String>> stack = DynamicArray.create();
    while (p < data.length) {
      if (!stack.isEmpty()) {
        p = skipLWS(data, p);
        if (data[p] != ';')
          break;
        ++p;
        p = skipLWS(data, p);
      }

      Option<ParseResult<KeyValuePair<String, String>>> optResult =
          CookieKvpParser.singleton.tryParse(data, p);
      if (optResult.isEmpty())
        break;
      ParseResult<KeyValuePair<String, String>> result = optResult.getOrThrow();
      stack.pushBack(result.getValue());
      p = result.getRem();
    }

    SequentialMultidictionary<String, String> result = ImmutableArrayMultidictionary.copyOf(stack);
    return Option.some(new ParseResult<>(result, p));
  }
}
