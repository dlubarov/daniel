package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.parsing.ParseResult;
import org.junit.Test;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

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
