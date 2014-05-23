package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.parsing.ParseResult;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HeaderSectionParserTest {
  @Test
  public void testTryParse() {
    String requesetString = ""
        + "foo\r\n"
        + "a: b\r\n"
        + "cd : ef\r\n\r\n"
        + "body";
    byte[] requestBytes = requesetString.getBytes(StandardCharsets.US_ASCII);

    Option<ParseResult<Sequence<KeyValuePair<String, String>>>> optResult =
        HeaderSectionParser.singleton.tryParse(requestBytes, 5);
    assertTrue(optResult.isDefined());
    ParseResult<Sequence<KeyValuePair<String, String>>> result = optResult.getOrThrow();

    Sequence<KeyValuePair<String, String>> expected = ImmutableArray.create(
        new KeyValuePair<>("a", "b"),
        new KeyValuePair<>("cd", "ef"));
    assertEquals(expected, result.getValue());

    assertEquals(18, result.getRem());
  }
}
