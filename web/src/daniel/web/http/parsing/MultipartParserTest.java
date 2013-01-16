package daniel.web.http.parsing;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.parsing.ParseResult;
import daniel.web.http.multipart.Part;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultipartParserTest {
  @Test
  public void testTryParse() {
    // From http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.2
    String rawData = ""
        + "--AaB03x\r\n"
        + "Content-Disposition: form-data; name=\"submit-name\"\r\n"
        + "\r\n"
        + "Larry\r\n"
        + "--AaB03x\r\n"
        + "Content-Disposition: form-data; name=\"files\"; filename=\"file1.txt\"\r\n"
        + "Content-Type: text/plain\r\n"
        + "\r\n"
        + "... contents of file1.txt ...\r\n"
        + "--AaB03x--";

    MultipartParser parser = new MultipartParser("AaB03x");
    byte[] rawBytes = rawData.getBytes(StandardCharsets.US_ASCII);
    Option<ParseResult<Sequence<Part>>> optResult = parser.tryParse(rawBytes, 0);
    assertTrue(optResult.isDefined());

    ParseResult<Sequence<Part>> result = optResult.getOrThrow();
    assertEquals(rawData.length(), result.getRem());

    Sequence<Part> parts = result.getValue();
    assertEquals(2, parts.getSize());
    Part firstPart = parts.get(0), secondPart = parts.get(1);

    // Check the first part.
    assertEquals(1, firstPart.getHeaders().getSize());
    assertEquals(
        new KeyValuePair<>("Content-Disposition", "form-data; name=\"submit-name\""),
        firstPart.getHeaders().get(0));
    assertArrayEquals(
        "Larry".getBytes(StandardCharsets.US_ASCII),
        firstPart.getBody());

    // Check the second part.
    assertEquals(2, secondPart.getHeaders().getSize());
    assertEquals(
        new KeyValuePair<>("Content-Disposition", "form-data; name=\"files\"; filename=\"file1.txt\""),
        secondPart.getHeaders().get(0));
    assertEquals(
        new KeyValuePair<>("Content-Type", "text/plain"),
        secondPart.getHeaders().get(1));
    assertArrayEquals(
        "... contents of file1.txt ...".getBytes(StandardCharsets.US_ASCII),
        secondPart.getBody());
  }
}
