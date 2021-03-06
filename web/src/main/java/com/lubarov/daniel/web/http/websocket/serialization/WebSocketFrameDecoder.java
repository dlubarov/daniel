package com.lubarov.daniel.web.http.websocket.serialization;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.util.Check;
import com.lubarov.daniel.data.util.IOUtils;
import com.lubarov.daniel.web.http.websocket.WebSocketFrame;
import com.lubarov.daniel.web.http.websocket.WebSocketOpcode;

import java.io.IOException;
import java.io.InputStream;

public final class WebSocketFrameDecoder {
  private WebSocketFrameDecoder() {}

  public static Option<WebSocketFrame> parseFrame(InputStream inputStream) throws IOException {
    WebSocketFrame.Builder builder = new WebSocketFrame.Builder();

    int firstByte = inputStream.read();
    if (firstByte == -1)
      return Option.none();
    builder.setFinalFragment((firstByte & 0b10000000) != 0);
    int opcodeValue = firstByte & 0b1111;
    builder.setOpcode(WebSocketOpcode.fromEncodedValue(opcodeValue));

    int secondByte = inputStream.read();
    Check.that(secondByte >= 0, "Unexpected end of stream.");
    boolean masked = (secondByte & 0b10000000) != 0;
    int lenFirstByte = secondByte & 0b1111111;
    int length;
    switch (lenFirstByte) {
      case 126:
        length = LengthSerializer.decodeLength(IOUtils.readFromStream(inputStream, 2));
        break;
      case 127:
        length = LengthSerializer.decodeLength(IOUtils.readFromStream(inputStream, 8));
        break;
      default:
        length = lenFirstByte;
    }

    if (masked)
      builder.setMaskingKey(IOUtils.readFromStream(inputStream, 4));
    builder.setPayload(IOUtils.readFromStream(inputStream, length));

    return Option.some(builder.build());
  }
}
