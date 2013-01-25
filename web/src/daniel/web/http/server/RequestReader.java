package daniel.web.http.server;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.data.util.IOUtils;
import daniel.web.http.HttpRequest;
import daniel.web.http.RequestHeaderName;
import daniel.web.http.RequestLine;
import daniel.web.http.parsing.HeaderSectionParser;
import daniel.web.http.parsing.RequestLineParser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

final class RequestReader {
  private RequestReader() {}

  public static Option<HttpRequest> readRequest(InputStream inputStream) throws IOException {
    Option<String> optRequestLine = readLineFromStream(inputStream);
    if (optRequestLine.isEmpty())
      return Option.none();
    String requestLineString = optRequestLine.getOrThrow();

    RequestLine requestLine = RequestLineParser.singleton
        .tryParse(requestLineString.getBytes(StandardCharsets.US_ASCII), 0)
        .getOrThrow("Failed to parse request line: %s.", requestLineString)
        .getValue();
    HttpRequest.Builder requestBuilder = new HttpRequest.Builder()
        .setMethod(requestLine.getMethod())
        .setResource(requestLine.getResource())
        .setHttpVersion(requestLine.getHttpVersion());
    StringBuilder rawHeadersBuilder = new StringBuilder();
    for (;;) {
      String line = readLineFromStream(inputStream)
          .getOrThrow("Unexpected end of stream");
      if (line.isEmpty())
        break;
      rawHeadersBuilder.append(line).append("\r\n");
    }
    Sequence<KeyValuePair<String, String>> headers = HeaderSectionParser.singleton
        .tryParse(rawHeadersBuilder.toString().getBytes(StandardCharsets.US_ASCII), 0)
        .getOrThrow().getValue();
    Option<Integer> optContentLength = Option.none();
    for (KeyValuePair<String, String> header : headers) {
      requestBuilder.addHeader(header);
      if (header.getKey().equals(RequestHeaderName.CONTENT_LENGTH.getStandardName()))
        optContentLength = Option.some(Integer.parseInt(header.getValue()));
    }
    if (optContentLength.isDefined()) {
      byte[] body = IOUtils.readFromStream(inputStream, optContentLength.getOrThrow());
      requestBuilder.setBody(body);
    }

    return Option.some(requestBuilder.build());
  }

  private static Option<String> readLineFromStream(InputStream inputStream) throws IOException {
    StringBuilder sb = new StringBuilder();
    for (;;) {
      int b = inputStream.read();
      if (b == -1)
        return Option.none();
      if (b == '\r') {
        if (inputStream.read() != '\n')
          throw new IllegalStateException("Expected LN after CR.");
        break;
      }
      sb.append((char) b);
    }
    return Option.some(sb.toString());
  }
}
