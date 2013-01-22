package daniel.web.http;

import daniel.data.collection.Collection;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.multidictionary.sequential.ImmutableArrayMultidictionary;
import daniel.data.multidictionary.sequential.ImmutableSequentialMultidictionary;
import daniel.data.multidictionary.sequential.SequentialMultidictionary;
import daniel.data.option.Option;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.web.http.cookies.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class HttpResponse {
  public static final class Builder {
    private Option<HttpVersion> httpVersion = Option.none();
    private Option<HttpStatus> status = Option.none();
    private final MutableStack<KeyValuePair<String, String>> headers;
    private Option<byte[]> body = Option.none();

    public Builder() {
      headers = DynamicArray.create();
    }

    public Builder setHttpVersion(HttpVersion httpVersion) {
      this.httpVersion = Option.some(httpVersion);
      return this;
    }

    public Builder setStatus(HttpStatus status) {
      this.status = Option.some(status);
      return this;
    }

    public Builder addHeader(KeyValuePair<String, String> header) {
      headers.pushBack(header);
      return this;
    }

    public Builder addHeader(ResponseHeaderName name, String value) {
      return addHeader(new KeyValuePair<>(name.getStandardName(), value));
    }

    public Builder addHeader(String name, String value) {
      return addHeader(new KeyValuePair<>(name, value));
    }

    public Builder addCookie(Cookie cookie) {
      StringBuilder sb = new StringBuilder();
      sb.append(cookie.getName()).append('=');
      try {
        sb.append(URLEncoder.encode(cookie.getValue(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        throw new AssertionError("wtf?");
      }
      if (cookie.getExpires().isDefined())
        sb.append("; Expires=").append(DateUtils.formatInstant(cookie.getExpires().getOrThrow()));
      return addHeader(ResponseHeaderName.SET_COOKIE, sb.toString());
    }

    public Builder addAllCookies(Collection<Cookie> cookies) {
      for (Cookie cookie : cookies)
        addCookie(cookie);
      return this;
    }

    public Builder setBody(byte[] body) {
      this.body = Option.some(body);
      return this;
    }

    public HttpResponse build() {
      return new HttpResponse(this);
    }
  }

  private final HttpVersion httpVersion;
  private final HttpStatus status;
  private final ImmutableSequentialMultidictionary<String, String> headers;
  private final Option<byte[]> body;

  private HttpResponse(Builder builder) {
    this.httpVersion = builder.httpVersion.getOrThrow("No HTTP version was set.");
    this.status = builder.status.getOrThrow("No response status was set.");
    this.headers = ImmutableArrayMultidictionary.copyOf(builder.headers);
    this.body = builder.body;
  }

  public HttpVersion getHttpVersion() {
    return httpVersion;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public SequentialMultidictionary<String, String> getHeaders() {
    return headers;
  }

  public Option<byte[]> getBody() {
    return body;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("HTTP/%s %s\r\n", httpVersion, status));
    for (KeyValuePair<String, String> header : headers)
      sb.append(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
    sb.append("\r\n");
    if (body.isDefined())
      sb.append(new String(body.getOrThrow(), StandardCharsets.US_ASCII));
    return sb.toString();
  }
}
