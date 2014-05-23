package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.web.http.HttpVersion;
import com.lubarov.daniel.web.http.RequestLine;
import com.lubarov.daniel.web.http.RequestMethod;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class RequestLineParserTest {
  private static final RequestLine example1 = new RequestLine.Builder()
      .setMethod(RequestMethod.GET)
      .setResource("/path/to/resource")
      .setHttpVersion(HttpVersion._1_0)
      .build();

  @Test
  public void testTryParse() {
    ParseResult<RequestLine> result = RequestLineParser.singleton
        .tryParse(example1.toString().getBytes(StandardCharsets.US_ASCII), 0)
        .getOrThrow("Could not parse.");
    assertEquals(example1.toString().length(), result.getRem());
    assertEquals(example1, result.getValue());
  }
}
