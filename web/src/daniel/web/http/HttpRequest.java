package daniel.web.http;

import daniel.data.collection.Collection;
import daniel.data.dictionary.KeyValuePair;
import daniel.data.dictionary.functions.GetKeyFunction;
import daniel.data.dictionary.functions.GetValueFunction;
import daniel.data.multidictionary.sequential.ImmutableArrayMultidictionary;
import daniel.data.multidictionary.sequential.SequentialMultidictionary;
import daniel.data.option.Option;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.data.util.Check;
import daniel.web.http.multipart.Part;
import daniel.web.http.parsing.MultipartParser;
import daniel.web.http.parsing.TokenOrQuotedStringParser;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
  private final SequentialMultidictionary<String, String> headers;
  private final Option<byte[]> body;

  private HttpRequest(Builder builder) {
    this.method = builder.method.getOrThrow("No request method was set.");
    this.resource = builder.resource.getOrThrow("No request resource was set.");
    this.httpVersion = builder.httpVersion.getOrThrow("No HTTP version was set.");
    this.headers = ImmutableArrayMultidictionary.copyOf(
        builder.headers.map(HttpHeader.toKeyValuePairFunction));
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

  public SequentialMultidictionary<String, String> getHeaders() {
    return headers;
  }

  public SequentialMultidictionary<String, String> getPostData() {
    String contentType = headers.getValues(RequestHeaderName.CONTENT_TYPE.getStandardName())
        .tryGetOnlyElement().getOrThrow("Expected exactly one content type.");
    Check.that(contentType.equals("application/x-www-form-urlencoded"),
        "Expected content type of \"%s\", but found \"%s\".",
        "application/x-www-form-urlencoded", contentType);

    String data = new String(body.getOrThrow("No POST data was sent."), StandardCharsets.US_ASCII);
    MutableStack<KeyValuePair<String, String>> keyValuePairs = DynamicArray.create();
    for (String param : data.split("&")) {
      String[] pair = param.split("=");
      Check.that(pair.length == 2, "Expected key=val format.");
      try {
        String key = URLDecoder.decode(pair[0], "UTF-8");
        String value = URLDecoder.decode(pair[1], "UTF-8");
        keyValuePairs.pushBack(new KeyValuePair<>(key, value));
      } catch (UnsupportedEncodingException e) {
        throw new AssertionError("UTF-8 should be supported universally.");
      }
    }
    return ImmutableArrayMultidictionary.copyOf(keyValuePairs);
  }

  public Collection<String> getPostValues(String name) {
    return getPostData().groupBy(new GetKeyFunction<String, String>())
        .getValue(name).map(new GetValueFunction<String, String>());
  }

  public ImmutableSequence<Part> getParts() {
    String contentType = headers.getValues(RequestHeaderName.CONTENT_TYPE.getStandardName())
        .tryGetOnlyElement().getOrThrow("Expected exactly one content type.");
    if (!contentType.startsWith("multipart/form-data"))
      throw new RuntimeException("Expected content type of multipart/form-data");

    // TODO: This could possibly fail if the content type is something like
    // Content-Type: multipart/form-data; foo="boundary="; boundary=[boundary]
    int pBoundary = contentType.indexOf("boundary=");
    if (pBoundary == -1)
      throw new RuntimeException("Expected boundary in content type.");
    pBoundary += "boundary=".length();
    String boundary = TokenOrQuotedStringParser.singleton
        .tryParse(contentType.substring(pBoundary).getBytes(StandardCharsets.US_ASCII), 0)
        .getOrThrow("Expected token or quoted string after \"boundary=\".").getValue();

    return new MultipartParser(boundary)
        .tryParse(body.getOrThrow("No body was sent."), 0)
        .getOrThrow("Multipart parsing failed")
        .getValue().toImmutable();
  }

  public String getHost() {
    return headers.getValues(RequestHeaderName.HOST.getStandardName())
        .tryGetOnlyElement().getOrThrow("Expected exactly one Host header.");
  }

  public Option<byte[]> getBody() {
    return body;
  }

  // TODO: Add methods for reading cookies.

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%s %s HTTP/%s", method, resource, httpVersion));
    for (KeyValuePair<String, String> header : headers)
      sb.append("\r\n").append(new HttpHeader(header));
    return sb.toString();
  }
}
