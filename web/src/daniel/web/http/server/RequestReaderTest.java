package daniel.web.http.server;

import daniel.web.http.HttpRequest;
import daniel.web.http.HttpVersion;
import daniel.web.http.RequestHeaderName;
import daniel.web.http.RequestMethod;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class RequestReaderTest {
  @Test
  public void testReadRequest() throws IOException {
    String requestString = ""
        + "GET /index.html HTTP/1.1\r\n"
        + "Host: www.example.com\r\n"
        + "Content-Type: application/x-www-form-urlencoded\r\n"
        + "Content-Length: 25\r\n"
        + "\r\n"
        + "username=foo&password=bar";

    byte[] requestBytes = requestString.getBytes(StandardCharsets.US_ASCII);
    HttpRequest request = RequestReader
        .readRequest(new ByteArrayInputStream(requestBytes))
        .getOrThrow();

    assertEquals(RequestMethod.GET, request.getMethod());
    assertEquals("/index.html", request.getResource());
    assertEquals(HttpVersion._1_1, request.getHttpVersion());

    String host = request.getHeaders().getValues(RequestHeaderName.HOST.getStandardName())
        .tryGetOnlyElement().getOrThrow();
    assertEquals("www.example.com", host);

    String username = request.getUrlencodedPostData().getValues("username")
        .tryGetOnlyElement().getOrThrow();
    assertEquals("foo", username);

    String password = request.getUrlencodedPostData().getValues("password")
        .tryGetOnlyElement().getOrThrow();
    assertEquals("bar", password);
  }
}
