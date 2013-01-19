package daniel.web.http.parsing;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.function.Function;
import daniel.data.multidictionary.sequential.ImmutableArrayMultidictionary;
import daniel.data.multidictionary.sequential.SequentialMultidictionary;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.parsing.CharacterParser;
import daniel.parsing.DelimitedRepetitionParser;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;

/**
 * Parses a cookie header value, for example "$Version=1; Skin=new;".
 */
public class CookieHeaderParser extends Parser<SequentialMultidictionary<String, String>> {
  public static final CookieHeaderParser singleton = new CookieHeaderParser();

  private static final Parser<Sequence<KeyValuePair<String, String>>> proxy =
      DelimitedRepetitionParser.repeatArbitrarily(
          CookieKvpHeader.singleton, new CharacterParser(';'));

  private CookieHeaderParser() {}

  @Override
  public Option<ParseResult<SequentialMultidictionary<String, String>>> tryParse(
      byte[] data, int p) {
    return proxy.tryParse(data, p).map(
        new Function<ParseResult<Sequence<KeyValuePair<String, String>>>, ParseResult<SequentialMultidictionary<String, String>>>() {
          @Override public ParseResult<SequentialMultidictionary<String, String>> apply(
              ParseResult<Sequence<KeyValuePair<String, String>>> parseResult) {
            return parseResult.map(new Function<Sequence<KeyValuePair<String, String>>, SequentialMultidictionary<String, String>>() {
              @Override public SequentialMultidictionary<String, String> apply(
                  Sequence<KeyValuePair<String, String>> cookies) {
                return ImmutableArrayMultidictionary.copyOf(cookies);
              }
            });
          }
        });
  }
}
