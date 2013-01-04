package daniel.web.http;

import daniel.data.option.Option;
import daniel.data.util.EqualsBuilder;
import daniel.data.util.HashCodeBuilder;

public final class RequestLine {
  public static class Builder {
    private Option<RequestMethod> method = Option.none();
    private Option<String> resource = Option.none();
    private Option<HttpVersion> httpVersion = Option.none();

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

    public RequestLine build() {
      return new RequestLine(this);
    }
  }

  private final RequestMethod method;
  private final String resource;
  private final HttpVersion httpVersion;

  private RequestLine(Builder builder) {
    method = builder.method.getOrThrow("Method not set.");
    resource = builder.resource.getOrThrow("Resource not set.");
    httpVersion = builder.httpVersion.getOrThrow("HTTP version not set.");
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

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof RequestLine))
      return false;

    RequestLine that = (RequestLine) o;
    return new EqualsBuilder()
        .append(this.method, that.method)
        .append(this.resource, that.resource)
        .append(this.httpVersion, that.httpVersion)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(method)
        .append(resource)
        .append(httpVersion)
        .toHashCode();
  }

  @Override
  public String toString() {
    return String.format("%s %s HTTP/%s", method, resource, httpVersion);
  }
}
