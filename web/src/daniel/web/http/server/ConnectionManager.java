package daniel.web.http.server;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.option.Option;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.logging.Logger;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.RequestMethod;
import daniel.web.http.cookies.CookieManager;
import daniel.web.http.websocket.AcceptKeyGenerator;
import daniel.web.http.websocket.WebSocketFrame;
import daniel.web.http.websocket.WebSocketMessage;
import daniel.web.http.websocket.WebSocketOpcode;
import daniel.web.http.websocket.serialization.WebSocketFrameDecoder;
import daniel.web.http.websocket.serialization.WebSocketFrameEncoder;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
      logger.info("Received empty request.");
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
    if (webSocketHandler.isEmpty()) {
      throw new RuntimeException("No WebSocket handler configured.");
    }

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

    continueWebsocketConversation(request);
  }

  // TODO: Move into another class & break up.
  private void continueWebsocketConversation(HttpRequest request) throws IOException {
    MutableStack<WebSocketFrame> fragments = DynamicArray.create();

    for (;;) {
      WebSocketFrame frame = WebSocketFrameDecoder.parseFrame(socket.getInputStream());
      switch (frame.getOpcode()) {
        case TEXT_FRAME:
        case BINARY_FRAME:
        case CONTINUATION:
          fragments.pushBack(frame);
          if (frame.isFinalFragment()) {
            WebSocketMessage message = WebSocketMessage.fromFragments(fragments);
            webSocketHandler.getOrThrow().handle(message, request);
            fragments = DynamicArray.create();
          }
          break;
        case CONNECTION_CLOSE:
          logger.info("WebSocket connection closed.");
          return;
        case PING:
          sendPong();
          break;
        case PONG:
          logger.warn("Received unexpected pong. Ignoring.");
          break;
        default:
          throw new AssertionError("Unexpected opcode.");
      }
    }
  }

  private void sendPong() throws IOException {
    logger.info("Sending pong");
    WebSocketFrame pongFrame = new WebSocketFrame.Builder()
        .setFinalFragment(true)
        .setOpcode(WebSocketOpcode.PONG)
        .setPayload(new byte[0])
        .build();
    WebSocketFrameEncoder.encodeFrame(pongFrame, socket.getOutputStream());
  }

  private void handleNormalRequest(HttpRequest request) throws IOException {
    Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);

    logger.info("Handling request for %s%s.",
        request.getHost(), request.getResource());
    CookieManager.resetCookies();
    HttpResponse response = handler.handle(request);

    writer.write(String.format("HTTP/%s %s\r\n", response.getHttpVersion(), response.getStatus()));
    for (KeyValuePair<String, String> header : response.getHeaders())
      writer.write(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
    writer.write("Connection: close\r\n");
    writer.write("\r\n");
    writer.flush();
    if (response.getBody().isDefined() && request.getMethod() != RequestMethod.HEAD)
      socket.getOutputStream().write(response.getBody().getOrThrow());
    socket.getOutputStream().flush();
    socket.close();
  }
}
