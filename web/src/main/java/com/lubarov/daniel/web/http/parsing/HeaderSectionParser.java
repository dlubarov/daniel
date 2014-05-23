package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.parsing.DelimitedRepetitionParser;
import com.lubarov.daniel.parsing.LiteralParser;
import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.parsing.Parser;

import java.nio.charset.StandardCharsets;

public final class HeaderSectionParser extends Parser<Sequence<KeyValuePair<String, String>>> {
  public static final HeaderSectionParser singleton = new HeaderSectionParser();

  private static final Parser<Sequence<KeyValuePair<String, String>>> proxy =
      DelimitedRepetitionParser.repeatArbitrarily(HeaderParser.singleton,
          new LiteralParser("\r\n", StandardCharsets.US_ASCII));

  private HeaderSectionParser() {}

  @Override
  public Option<ParseResult<Sequence<KeyValuePair<String, String>>>> tryParse(byte[] data, int p) {
    return proxy.tryParse(data, p);
  }
}
