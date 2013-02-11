package daniel.web.http.parsing;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.table.sequential.SequentialTable;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CookieHeaderParserTest {
  @Test
  public void testTryParse() {
    String cookieHeader = "$Version=1; Skin=new;";
    byte[] cookieHeaderBytes = cookieHeader.getBytes(StandardCharsets.US_ASCII);
    SequentialTable<String, String> result =
        CookieHeaderParser.singleton.tryParse(cookieHeaderBytes, 0)
            .getOrThrow().getValue();

    assertEquals(2, result.getSize());
    KeyValuePair<String, String> kvp1 = result.get(0);
    KeyValuePair<String, String> kvp2 = result.get(1);

    assertEquals(new KeyValuePair<>("$Version", "1"), kvp1);
    assertEquals(new KeyValuePair<>("Skin", "new"), kvp2);
  }
}
