package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.parsing.ParseResult;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenParserTest {
  @Test
  public void testTryParse() {
    String headerString = "ab-cd: ef(gh)";
    byte[] headerBytes = headerString.getBytes(StandardCharsets.US_ASCII);

    Option<ParseResult<String>> optResult = TokenParser.singleton.tryParse(headerBytes, 0);
    assertTrue(optResult.isDefined());
    assertEquals("ab-cd", optResult.getOrThrow().getValue());
    assertEquals(5, optResult.getOrThrow().getRem());
  }
}
