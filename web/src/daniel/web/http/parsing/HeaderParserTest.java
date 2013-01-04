package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.parsing.ParseResult;
import daniel.web.http.HttpHeader;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

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

    Option<ParseResult<HttpHeader>> optResult1 = HeaderParser.singleton.tryParse(requestBytes, 5);
    assertTrue(optResult1.isDefined());
    assertEquals(new HttpHeader("a", "b"), optResult1.getOrThrow().getValue());
    assertEquals(9, optResult1.getOrThrow().getRem());

    Option<ParseResult<HttpHeader>> optResult2 = HeaderParser.singleton.tryParse (requestBytes, 11);
    assertTrue(optResult2.isDefined());
    assertEquals(new HttpHeader("cd", "ef gh"), optResult2.getOrThrow().getValue());
    assertEquals(25, optResult2.getOrThrow().getRem());
  }
}
