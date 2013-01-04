package daniel.web.http.server;

import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.data.util.IOUtils;
import daniel.web.http.HttpHeader;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.RequestHeaderName;
import daniel.web.http.RequestLine;
import daniel.web.http.RequestMethod;
import daniel.web.http.parsing.HeaderSectionParser;
import daniel.web.http.parsing.RequestLineParser;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

// TODO: Refactor ConnectionManager.
public class ConnectionManager implements Runnable {
  private final Socket socket;
  private final Handler handler;

  public ConnectionManager(Socket socket, Handler handler) {
    this.socket = socket;
    this.handler = handler;
  }

  @Override
  public void run() {
    try {
      runWithExceptions();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void runWithExceptions() throws Exception {
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
    Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);

    String rawRequestLine = bufferedReader.readLine();
    if (rawRequestLine == null)
      throw new EOFException("Unexpected EOF.");

    RequestLine requestLine = RequestLineParser.singleton
        .tryParse(rawRequestLine.getBytes(StandardCharsets.US_ASCII), 0)
        .getOrThrow("Failed to parse request line.")
        .getValue();
    HttpRequest.Builder requestBuilder = new HttpRequest.Builder()
        .setMethod(requestLine.getMethod())
        .setResource(requestLine.getResource())
        .setHttpVersion(requestLine.getHttpVersion());
    StringBuilder rawHeadersBuilder = new StringBuilder();
    for (;;) {
      String line = bufferedReader.readLine();
      if (line == null)
        throw new EOFException();
      if (line.isEmpty())
        break;
      rawHeadersBuilder.append(line).append("\r\n");
    }
    Sequence<HttpHeader> headers = HeaderSectionParser.singleton
        .tryParse(rawHeadersBuilder.toString().getBytes(StandardCharsets.US_ASCII), 0)
        .getOrThrow().getValue();
    Option<Integer> optContentLength = Option.none();
    for (HttpHeader header : headers) {
      requestBuilder.addHeader(header);
      if (header.getName().equals(RequestHeaderName.CONTENT_LENGTH.getStandardName()))
        optContentLength = Option.some(Integer.parseInt(header.getValue()));
    }
    if (optContentLength.isDefined()) {
      byte[] body = IOUtils.readFromStream(socket.getInputStream(), optContentLength.getOrThrow());
      requestBuilder.setBody(body);
    }
    HttpRequest request = requestBuilder.build();

    System.out.println("Handling request for " + request.getResource());
    HttpResponse response = handler.handle(request);

    writer.write(String.format("HTTP/%s %s\r\n", response.getHttpVersion(), response.getStatus()));
    for (HttpHeader header : response.getHeaders())
      writer.write(header + "\r\n");
    writer.write("\r\n");
    if (response.getBody().isDefined() && request.getMethod() != RequestMethod.HEAD)
      socket.getOutputStream().write(response.getBody().getOrThrow());
    socket.getOutputStream().flush();
    socket.close();
  }
}
