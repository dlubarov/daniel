package daniel.web.http.server;

import daniel.web.html.Attribute;
import daniel.web.html.Xhtml5Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.DateUtils;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.HttpVersion;
import daniel.web.http.ResponseHeaderName;
import daniel.web.http.cookies.CookieManager;
import java.nio.charset.Charset;
import java.util.Date;

public final class HttpResponseFactory {
  private HttpResponseFactory() {}

  public static HttpResponse htmlResponse(HttpStatus status, Xhtml5Document document) {
    byte[] documentBytes = document.toString().getBytes(Charset.forName("UTF-8"));
    return new HttpResponse.Builder()
        .setStatus(status)
        .setHttpVersion(HttpVersion._1_1)
        .addHeader(ResponseHeaderName.DATE, DateUtils.formatDate(new Date()))
        .addHeader(ResponseHeaderName.EXPIRES, "Thu, 19 Nov 1981 08:52:00 GMT")
        .addHeader(ResponseHeaderName.PRAGMA, "no-cache")
        .addHeader(ResponseHeaderName.CACHE_CONTROL, "no-store, no-cache, must-revalidate, post-check=0, pre-check=0")
        .addHeader(ResponseHeaderName.CONTENT_LENGTH, Integer.toString(documentBytes.length))
        .addHeader(ResponseHeaderName.CONTENT_TYPE, "application/xhtml+xml; charset=utf-8")
        .addAllCookies(CookieManager.getCookies())
        .setBody(documentBytes)
        .build();
  }

  public static HttpResponse permanentRedirect(String location) {
    return redirect(location, HttpStatus.MOVED_PERMANENTLY);
  }

  public static HttpResponse redirectToGet(String location) {
    return redirect(location, HttpStatus.SEE_OTHER);
  }

  public static HttpResponse redirectSameMethod(String location) {
    return redirect(location, HttpStatus.TEMPORARY_REDIRECT);
  }

  private static HttpResponse redirect(String location, HttpStatus status) {
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
    Element html = new Element.Builder(Tag.HTML)
        .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setAttribute("xml:lang", "en")
        .addChild(head)
        .addChild(body)
        .build();

    Xhtml5Document document = new Xhtml5Document(html);
    byte[] documentBytes = document.toString().getBytes(Charset.forName("UTF-8"));

    return new HttpResponse.Builder()
        .setStatus(status)
        .setHttpVersion(HttpVersion._1_1)
        .addHeader(ResponseHeaderName.DATE, DateUtils.formatDate(new Date()))
        .addHeader(ResponseHeaderName.LOCATION, location)
        .addHeader(ResponseHeaderName.CONTENT_LENGTH, Integer.toString(documentBytes.length))
        .addHeader(ResponseHeaderName.CONTENT_TYPE, "text/html; charset=utf-8")
        .addAllCookies(CookieManager.getCookies())
        .setBody(documentBytes)
        .build();
  }
}
