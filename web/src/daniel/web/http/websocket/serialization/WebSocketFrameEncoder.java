package daniel.web.http.websocket.serialization;

import daniel.web.http.websocket.WebSocketFrame;
import java.io.IOException;
import java.io.OutputStream;

public final class WebSocketFrameEncoder {
  private WebSocketFrameEncoder() {}

  public static void encodeFrame(WebSocketFrame frame, OutputStream outputStream)
      throws IOException {
    int firstByte = (frame.isFinalFragment() ? 0b10000000 : 0)
        | frame.getOpcode().getEncodedValue();
    outputStream.write(firstByte);


    int len = frame.getPayload().length;
    int lenFirstByte = len > (2 << 16)
        ? 127
        : (len >= 126 ? 126 : len);

    int secondByte = (frame.getMaskingKey().isDefined() ? 0b10000000 : 0) | lenFirstByte;
    outputStream.write(secondByte);

    if (lenFirstByte == 126)
      outputStream.write(LengthSerializer.encodeLength(len, 2));
    if (lenFirstByte == 127)
      outputStream.write(LengthSerializer.encodeLength(len, 8));

    if (frame.getMaskingKey().isDefined())
      outputStream.write(frame.getMaskingKey().getOrThrow());

    outputStream.write(frame.getPayload());
  }
}
