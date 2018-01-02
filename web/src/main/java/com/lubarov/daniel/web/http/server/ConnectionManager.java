package com.lubarov.daniel.web.http.server;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.table.MutableHashTable;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.data.util.CompressionUtils;
import com.lubarov.daniel.common.Logger;
import com.lubarov.daniel.web.http.*;
import com.lubarov.daniel.web.http.compression.AcceptEncodingParser;
import com.lubarov.daniel.web.http.cookies.CookieManager;
import com.lubarov.daniel.web.http.websocket.AcceptKeyGenerator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

// TODO: Split this logic up more.
final class ConnectionManager implements Runnable {
  private static final Logger logger = Logger.forClass(ConnectionManager.class);

  private final Socket socket;
  private final Handler handler;
  private final Option<WebSocketHandler> webSocketHandler;

  public ConnectionManager(Socket socket, Handler handler,
      Option<WebSocketHandler> webSocketHandler) {
    this.socket = socket;
    this.handler = handler;
    this.webSocketHandler = webSocketHandler;
  }

  @Override
  public void run() {
    try {
      runWithExceptions();
    } catch (Exception e) {
      logger.error(e, "Exception in connection management.");
    }
  }

  private void runWithExceptions() throws IOException {
    Option<HttpRequest> optRequest = RequestReader.readRequest(socket.getInputStream());
    if (optRequest.isEmpty()) {
      logger.debug("Received empty request.");
      return;
    }

    HttpRequest request = optRequest.getOrThrow();
    boolean upgrade = request.getHeaders().getValues("Connection").contains("Upgrade");
    boolean websocket = upgrade && request.getHeaders().getValues("Upgrade").contains("websocket");

    if (websocket)
      handleWebsocketRequest(request);
    else
      handleNormalRequest(request);
  }

  private void handleWebsocketRequest(HttpRequest request) throws IOException {
    if (webSocketHandler.isEmpty())
      throw new RuntimeException("No WebSocket handler configured.");

    logger.info("Handling WebSocket request for %s%s.", request.getHost(), request.getResource());

    String clientKey = request.getHeaders().getValues("Sec-WebSocket-Key")
        .tryGetOnlyElement().getOrThrow("Expected exactly one websocket key.");
    String accept = AcceptKeyGenerator.generateAcceptKey(clientKey);

    Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);
    writer.write("HTTP/1.1 101 Switching Protocols\r\n");
    writer.write("Upgrade: websocket\r\n");
    writer.write("Connection: Upgrade\r\n");
    writer.write("Sec-WebSocket-Accept: " + accept + "\r\n");
    writer.write("\r\n");
    writer.flush();

    new WebSocketManager(request, socket, webSocketHandler.getOrThrow()).listen();
  }

  private void handleNormalRequest(HttpRequest request) throws IOException {
    Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);

    logger.info("Handling %s request for %s%s.", request.getHttpVersion(), request.getHost(), request.getResource());
    logger.info("User agents: %s.", request.getHeaders().getValues("User-Agent"));
    logger.info("All headers: %s.", request.getHeaders());
    CookieManager.resetCookies();
    HttpResponse response = handler.handle(request);

    // TODO: This breaks header order, which is okay but not ideal.
    MutableHashTable<String, String> responseHeaders =
        MutableHashTable.copyOf(response.getHeaders());

    boolean gzipAccepted = AcceptEncodingParser.getAcceptedEncodings(request.getHeaders()).contains("gzip");
    String defaultContentEncoding = gzipAccepted ? "gzip" : "identity";
    String contentEncodingString = responseHeaders.getValues("Content-Encoding")
        .tryGetOnlyElement().getOrDefault(defaultContentEncoding);
    final ContentEncoding contentEncoding = ContentEncoding.valueOf(contentEncodingString.toUpperCase());

    Option<byte[]> encodedContent = response.getBody().map(rawContent -> {
      switch (contentEncoding) {
        case IDENTITY:
          return rawContent;
        case GZIP:
          return CompressionUtils.gzip(rawContent);
        default:
          throw new AssertionError("Unexpected content encoding: " + contentEncoding);
      }
    });

    if (!responseHeaders.containsKey("Server"))
      responseHeaders.put("Server", "daniel");

    if (!responseHeaders.containsKey("Date"))
      responseHeaders.put("Date", DateUtils.formatRfc1123(Instant.now()));

    if (!responseHeaders.containsKey("Content-Encoding") && contentEncoding != ContentEncoding.IDENTITY)
      responseHeaders.put("Content-Encoding", contentEncoding.name().toLowerCase());

    if (!responseHeaders.containsKey("Vary"))
      responseHeaders.put("Vary", "Accept-Encoding");

    if (encodedContent.isDefined() && !responseHeaders.containsKey("Content-Length"))
      responseHeaders.put("Content-Length", Integer.toString(encodedContent.getOrThrow().length));

    if (!responseHeaders.containsKey("Connection"))
      responseHeaders.put("Connection", "close");

    // Write headers.
    writer.write(String.format("HTTP/%s %s\r\n", response.getHttpVersion(), response.getStatus()));
    for (KeyValuePair<String, String> header : responseHeaders)
      writer.write(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
    writer.write("\r\n");
    writer.flush();

    // Write body.
    if (encodedContent.isDefined() && request.getMethod() != RequestMethod.HEAD)
      socket.getOutputStream().write(encodedContent.getOrThrow());

    socket.close();
  }
}
