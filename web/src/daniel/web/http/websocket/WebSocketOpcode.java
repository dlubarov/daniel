package daniel.web.http.websocket;

public enum WebSocketOpcode {
  CONTINUATION(0),
  TEXT_FRAME(1),
  BINARY_FRAME(2),
  CONNECTION_CLOSE(8),
  PING(9),
  PONG(0xA);

  private final int encodedValue;

  private WebSocketOpcode(int encodedValue) {
    this.encodedValue = encodedValue;
  }

  public static WebSocketOpcode fromEncodedValue(int encodedValue) {
    for (WebSocketOpcode opcode : WebSocketOpcode.values())
      if (opcode.encodedValue == encodedValue)
        return opcode;
    throw new IllegalArgumentException("Unrecognized opcode value: " + encodedValue);
  }

  public int getEncodedValue() {
    return encodedValue;
  }
}
