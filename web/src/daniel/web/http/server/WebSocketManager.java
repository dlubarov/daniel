package daniel.web.http.server;

import daniel.data.option.Option;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.logging.Logger;
import daniel.web.http.HttpRequest;
import daniel.web.http.websocket.WebSocketFrame;
import daniel.web.http.websocket.WebSocketMessage;
import daniel.web.http.websocket.WebSocketOpcode;
import daniel.web.http.websocket.serialization.WebSocketFrameDecoder;
import daniel.web.http.websocket.serialization.WebSocketFrameEncoder;
import java.io.IOException;
import java.net.Socket;

/**
 * Manages a single WebSocket connection.
 *
 * TODO: Needs locking around output stream.
 */
public final class WebSocketManager {
  private static final Logger logger = Logger.forClass(WebSocketManager.class);

  private final HttpRequest request;
  private final Socket socket;
  private WebSocketHandler handler;

  public WebSocketManager(HttpRequest request, Socket socket, WebSocketHandler handler) {
    this.request = request;
    this.socket = socket;
    this.handler = handler;
  }

  public HttpRequest getRequest() {
    return request;
  }

  public void send(WebSocketFrame frame) {
    try {
      WebSocketFrameEncoder.encodeFrame(frame, socket.getOutputStream());
    } catch (IOException e) {
      logger.error(e, "Failed to send WebSocket frame.");
    }
  }

  public void listen() throws IOException {
    handler.onConnect(this);

    MutableStack<WebSocketFrame> fragments = DynamicArray.create();

    listeningLoop:
    for (;;) {
      Option<WebSocketFrame> optFrame = WebSocketFrameDecoder.parseFrame(socket.getInputStream());
      if (optFrame.isEmpty())
        break;
      WebSocketFrame frame = optFrame.getOrThrow();

      switch (frame.getOpcode()) {
        case TEXT_FRAME:
        case BINARY_FRAME:
        case CONTINUATION:
          fragments.pushBack(frame);
          if (frame.isFinalFragment()) {
            handler.handle(this, WebSocketMessage.fromFragments(fragments));
            fragments = DynamicArray.create();
          }
          break;
        case PING:
          sendPong();
          break;
        case PONG:
          logger.warn("Received unexpected pong. Ignoring.");
          break;
        case CONNECTION_CLOSE:
          logger.info("WebSocket connection closed.");
          socket.close();
          break listeningLoop;
        default:
          throw new AssertionError("Unexpected opcode.");
      }
    }

    handler.onDisconnect(this);
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
}
