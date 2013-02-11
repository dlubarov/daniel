package daniel.data.util;

import java.nio.charset.StandardCharsets;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DigestUtilsTest {
  @Test
  public void testMd5Hex() {
    String hex = DigestUtils.md5Hex("daniel@lubarov.com".getBytes(StandardCharsets.US_ASCII));
    assertEquals("dcfb9e0cd502e1a8948a7ca30da1469a", hex);
  }
}