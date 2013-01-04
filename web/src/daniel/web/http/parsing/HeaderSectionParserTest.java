package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.Sequence;
import daniel.parsing.ParseResult;
import daniel.web.http.HttpHeader;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

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

    Option<ParseResult<Sequence<HttpHeader>>> optResult =
        HeaderSectionParser.singleton.tryParse(requestBytes, 5);
    assertTrue(optResult.isDefined());
    ParseResult<Sequence<HttpHeader>> result = optResult.getOrThrow();

    Sequence<HttpHeader> expected = ImmutableArray.create(
        new HttpHeader("a", "b"),
        new HttpHeader("cd", "ef"));
    assertEquals(expected, result.getValue());

    assertEquals(18, result.getRem());
  }
}
