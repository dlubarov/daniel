package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.parsing.ParseResult;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HeaderParserTest {
  @Test
  public void testTryParse() {
    String requesetString = ""
        + "foo\r\n"
        + "a: b\r\n"
        + "cd : ef\r\n"
        + "\t  gh\r\n\r\n"
        + "body";
    byte[] requestBytes = requesetString.getBytes(StandardCharsets.US_ASCII);

    Option<ParseResult<KeyValuePair<String, String>>> optResult1 = HeaderParser.singleton.tryParse(requestBytes, 5);
    assertTrue(optResult1.isDefined());
    assertEquals(new KeyValuePair<>("a", "b"), optResult1.getOrThrow().getValue());
    assertEquals(9, optResult1.getOrThrow().getRem());

    Option<ParseResult<KeyValuePair<String, String>>> optResult2 = HeaderParser.singleton.tryParse (requestBytes, 11);
    assertTrue(optResult2.isDefined());
    assertEquals(new KeyValuePair<>("cd", "ef gh"), optResult2.getOrThrow().getValue());
    assertEquals(25, optResult2.getOrThrow().getRem());
  }
}
