package daniel.web.http;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestTest {
  @Test
  public void testGetHost() {
    HttpRequest request = new HttpRequest.Builder()
        .setMethod(RequestMethod.GET)
        .setResource("/resource")
        .setHttpVersion(HttpVersion._1_1)
        .addHeader("host", "www.example.com")
        .build();
    assertEquals("www.example.com", request.getHost());
  }
}
