package daniel.web.http.websocket;

public enum WebSocketMessageType {
  TEXT, BINARY;

  public static WebSocketMessageType fromOpcode(WebSocketOpcode opcode) {
    switch (opcode) {
      case TEXT_FRAME:
        return TEXT;
      case BINARY_FRAME:
        return BINARY;
      default:
        throw new IllegalArgumentException(String.format("%s is not a message type.", opcode));
    }
  }
}
