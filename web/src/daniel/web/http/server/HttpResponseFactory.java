package daniel.web.http.server;

import daniel.data.collection.Collection;
import daniel.data.sequence.ImmutableArray;
import daniel.web.html.Document;
import daniel.web.http.Cookie;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.HttpVersion;
import daniel.web.http.ResponseHeaderName;
import java.nio.charset.Charset;

public final class HttpResponseFactory {
  private HttpResponseFactory() {}

  public static HttpResponse htmlResponse(
      HttpStatus status,
      Document document,
      Collection<Cookie> cookies) {
    byte[] documentBytes = document.toString().getBytes(Charset.forName("UTF-8"));
    return new HttpResponse.Builder()
        .setStatus(status)
        .setHttpVersion(HttpVersion._1_1)
        .addHeader(ResponseHeaderName.CONTENT_TYPE, "application/xhtml+xml; charset=utf-8")
        .addHeader(ResponseHeaderName.EXPIRES, "Thu, 19 Nov 1981 08:52:00 GMT")
        .addHeader(ResponseHeaderName.PRAGMA, "no-cache")
        .addHeader(ResponseHeaderName.CACHE_CONTROL, "no-store, no-cache, must-revalidate, post-check=0, pre-check=0")
        .addHeader(ResponseHeaderName.CONTENT_LENGTH, Integer.toString(documentBytes.length))
        .addAllCookies(cookies)
        .setBody(documentBytes)
        .build();
  }

  public static HttpResponse htmlResponse(HttpStatus status, Document document) {
    return htmlResponse(status, document, ImmutableArray.<Cookie>create());
  }
}
