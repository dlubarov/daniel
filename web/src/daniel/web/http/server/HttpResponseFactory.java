package daniel.web.http.server;

import daniel.data.collection.Collection;
import daniel.data.sequence.ImmutableArray;
import daniel.web.html.Attribute;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.Cookie;
import daniel.web.http.DateUtils;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.HttpVersion;
import daniel.web.http.ResponseHeaderName;
import java.nio.charset.Charset;
import java.util.Date;

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
        .addHeader(ResponseHeaderName.DATE, DateUtils.formatDate(new Date()))
        .addHeader(ResponseHeaderName.EXPIRES, "Thu, 19 Nov 1981 08:52:00 GMT")
        .addHeader(ResponseHeaderName.PRAGMA, "no-cache")
        .addHeader(ResponseHeaderName.CACHE_CONTROL, "no-store, no-cache, must-revalidate, post-check=0, pre-check=0")
        .addHeader(ResponseHeaderName.CONTENT_LENGTH, Integer.toString(documentBytes.length))
        .addHeader(ResponseHeaderName.CONTENT_TYPE, "text/html; charset=utf-8")
        .addAllCookies(cookies)
        .setBody(documentBytes)
        .build();
  }

  public static HttpResponse htmlResponse(HttpStatus status, Document document) {
    return htmlResponse(status, document, ImmutableArray.<Cookie>create());
  }

  public static HttpResponse redirect(String location, boolean temporary) {
    HttpStatus status = temporary ? HttpStatus.TEMPORARY_REDIRECT : HttpStatus.MOVED_PERMANENTLY;

    Element head = new Element(Tag.HEAD, new Element(Tag.TITLE,
        TextNode.escapedText(status.toString())));
    Element body = new Element(Tag.BODY,
        new Element(Tag.H1, TextNode.escapedText(status.toString())),
        new Element(Tag.P,
            TextNode.escapedText("This document has been moved "),
            new Element.Builder(Tag.A)
                .setAttribute(Attribute.HREF, location)
                .addChild(TextNode.escapedText("here"))
                .build(),
            TextNode.escapedText(".")));
    Element html = new Element(Tag.HTML, head, body);

    Document document = new Document("<!DOCTYPE html>", html);
    byte[] documentBytes = document.toString().getBytes(Charset.forName("UTF-8"));

    return new HttpResponse.Builder()
        .setStatus(status)
        .setHttpVersion(HttpVersion._1_1)
        .addHeader(ResponseHeaderName.DATE, DateUtils.formatDate(new Date()))
        .addHeader(ResponseHeaderName.LOCATION, location)
        .addHeader(ResponseHeaderName.CONTENT_LENGTH, Integer.toString(documentBytes.length))
        .addHeader(ResponseHeaderName.CONTENT_TYPE, "text/html; charset=utf-8")
        .setBody(documentBytes)
        .build();
  }
}
