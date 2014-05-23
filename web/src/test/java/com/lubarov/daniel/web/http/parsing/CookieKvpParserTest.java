package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.parsing.ParseResult;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class CookieKvpParserTest {
  @Test
  public void testTryParse() {
    String cookieKvp = "a=b; session_id=abc-def-ghi; Expires=Wed, 09-Jun-2021 10:18:14 GMT";
    ParseResult<KeyValuePair<String, String>> resKvp = CookieKvpParser.singleton
        .tryParse(cookieKvp.getBytes(StandardCharsets.US_ASCII), 5).getOrThrow();
    KeyValuePair<String, String> kvp = resKvp.getValue();

    assertEquals("session_id", kvp.getKey());
    assertEquals("abc-def-ghi", kvp.getValue());
  }
}
