package daniel.web.http;

import daniel.data.collection.Collection;
import daniel.data.option.Option;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public class HttpResponse {
  public static class Builder {
    private Option<HttpVersion> httpVersion = Option.none();
    private Option<HttpStatus> status = Option.none();
    private final MutableStack<HttpHeader> headers;
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

    public Builder addHeader(HttpHeader header) {
      headers.pushBack(header);
      return this;
    }

    public Builder addHeader(ResponseHeaderName name, String value) {
      return addHeader(new HttpHeader(name, value));
    }

    public Builder addHeader(String name, String value) {
      return addHeader(new HttpHeader(name, value));
    }

    public Builder addCookie(Cookie cookie) {
      return addHeader(
          ResponseHeaderName.SET_COOKIE,
          String.format("%s=%s; Expires=%s",
              cookie.getName(), cookie.getValue(), cookie.getExpires()));
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
  private final ImmutableSequence<HttpHeader> headers;
  private final Option<byte[]> body;

  private HttpResponse(Builder builder) {
    this.httpVersion = builder.httpVersion.getOrThrow("No HTTP version was set.");
    this.status = builder.status.getOrThrow("No response status was set.");
    this.headers = builder.headers.toImmutable();
    this.body = builder.body;
  }

  public HttpVersion getHttpVersion() {
    return httpVersion;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public ImmutableSequence<HttpHeader> getHeaders() {
    return headers;
  }

  public Collection<String> getHeaderValues(String name) {
    Collection<HttpHeader> headers = getHeaders().groupBy(HttpHeader.getNameFunction).getValue(name);
    return headers.map(HttpHeader.getValueFunction);
  }

  public Collection<String> getHeaderValues(ResponseHeaderName name) {
    return getHeaderValues(name.getStandardName());
  }

  public Option<byte[]> getBody() {
    return body;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("HTTP/%s %s\r\n", httpVersion, status));
    for (HttpHeader header : headers)
      sb.append(header).append("\r\n");
    sb.append("\r\n");
    if (body.isDefined())
      sb.append(new String(body.getOrThrow()));
    return sb.toString();
  }
}
