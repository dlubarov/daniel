package daniel.web.http.parsing;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.table.sequential.SequentialTable;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CookieHeaderParserTest {
  @Test
  public void testTryParse_basic() {
    String cookieHeader = "$Version=1; Skin=new;";
    byte[] cookieHeaderBytes = cookieHeader.getBytes(StandardCharsets.US_ASCII);
    SequentialTable<String, String> result =
        CookieHeaderParser.singleton.tryParse(cookieHeaderBytes, 0)
            .getOrThrow().getValue();

    assertEquals(2, result.getSize());
    assertEquals(new KeyValuePair<>("$Version", "1"), result.get(0));
    assertEquals(new KeyValuePair<>("Skin", "new"), result.get(1));
  }

  @Test
  public void testTryParse_googleAnalytics() {
    String cookieHeader = "__utma=11106849.1375890264.1363113408.1363113408.1363113408.1; __utmb=11106849.1.10.1363113408; __utmc=11106849; __utmz=11106849.1363113408.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";
    byte[] cookieHeaderBytes = cookieHeader.getBytes(StandardCharsets.US_ASCII);
    SequentialTable<String, String> result =
        CookieHeaderParser.singleton.tryParse(cookieHeaderBytes, 0)
            .getOrThrow().getValue();

    assertEquals(4, result.getSize());
    assertEquals(new KeyValuePair<>("__utma", "11106849.1375890264.1363113408.1363113408.1363113408.1"), result.get(0));
    assertEquals(new KeyValuePair<>("__utmz", "11106849.1363113408.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)"),
        result.get(3));
  }
}
