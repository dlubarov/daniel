package daniel.web.http;

import daniel.data.collection.Collection;
import daniel.data.option.Option;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public final class HttpRequest {
  public static class Builder {
    private Option<RequestMethod> method = Option.none();
    private Option<String> resource = Option.none();
    private Option<HttpVersion> httpVersion = Option.none();
    private final MutableStack<HttpHeader> headers;
    private Option<byte[]> body = Option.none();

    public Builder() {
      headers = DynamicArray.create();
    }

    public Builder setMethod(RequestMethod method) {
      this.method = Option.some(method);
      return this;
    }

    public Builder setResource(String resource) {
      this.resource = Option.some(resource);
      return this;
    }

    public Builder setHttpVersion(HttpVersion httpVersion) {
      this.httpVersion = Option.some(httpVersion);
      return this;
    }

    public Builder addHeader(HttpHeader header) {
      headers.pushBack(header);
      return this;
    }

    public Builder addHeader(RequestHeaderName name, String value) {
      return addHeader(new HttpHeader(name, value));
    }

    public Builder addHeader(String name, String value) {
      return addHeader(new HttpHeader(name, value));
    }

    public Builder setBody(byte[] body) {
      this.body = Option.some(body);
      return this;
    }

    public HttpRequest build() {
      return new HttpRequest(this);
    }
  }

  private final RequestMethod method;
  private final String resource;
  private final HttpVersion httpVersion;
  private final ImmutableSequence<HttpHeader> headers;
  private final Option<byte[]> body;

  private HttpRequest(Builder builder) {
    this.method = builder.method.getOrThrow("No request method was set.");
    this.resource = builder.resource.getOrThrow("No request resource was set.");
    this.httpVersion = builder.httpVersion.getOrThrow("No HTTP version was set.");
    this.headers = builder.headers.toImmutable();
    this.body = builder.body;
  }

  public RequestMethod getMethod() {
    return method;
  }

  public String getResource() {
    return resource;
  }

  public HttpVersion getHttpVersion() {
    return httpVersion;
  }

  public ImmutableSequence<HttpHeader> getHeaders() {
    return headers;
  }

  public Collection<String> getHeaderValues(String name) {
    Collection<HttpHeader> headers = getHeaders().groupBy(HttpHeader.getNameFunction).getValue(name);
    return headers.map(HttpHeader.getValueFunction);
  }

  public Collection<String> getHeaderValues(RequestHeaderName name) {
    return getHeaderValues(name.getStandardName());
  }

  public String getHost() {
    return getHeaderValues(RequestHeaderName.HOST).tryGetOnlyElement()
        .getOrThrow("Expected exactly one Host header.");
  }

  public Option<byte[]> getBody() {
    return body;
  }

  // TODO: Add methods for reading cookies.

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%s %s HTTP/%s", method, resource, httpVersion));
    for (HttpHeader header : headers)
      sb.append("\r\n").append(header);
    return sb.toString();
  }
}
