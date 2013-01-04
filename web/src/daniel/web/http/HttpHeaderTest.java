package daniel.web.http;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpHeaderTest {
  @Test
  public void testEquals() {
    assertEquals(
        new HttpHeader("name", "value"),
        new HttpHeader("name", "value"));
  }

  @Test
  public void testHashCode() {
    assertEquals(
        new HttpHeader("name", "value").hashCode(),
        new HttpHeader("name", "value").hashCode());
  }

  @Test
  public void testToString() {
    assertEquals(
        "Host: www.example.com",
        new HttpHeader("Host", "www.example.com").toString());
  }
}
