package com.lubarov.daniel.web.http.websocket.serialization;

import com.lubarov.daniel.data.serialization.ByteSink;
import com.lubarov.daniel.data.serialization.ByteSource;
import com.lubarov.daniel.data.serialization.Serializer;
import com.lubarov.daniel.web.http.websocket.WebSocketFrame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WebSocketFrameSerializer implements Serializer<WebSocketFrame> {
  public static final WebSocketFrameSerializer singleton = new WebSocketFrameSerializer();

  private WebSocketFrameSerializer() {}

  @Override
  public void writeToSink(WebSocketFrame frame, ByteSink sink) {
    sink.giveAll(writeToByteArray(frame));
  }

  @Override
  public WebSocketFrame readFromSource(final ByteSource source) {
    try {
      return WebSocketFrameDecoder.parseFrame(new InputStream() {
        @Override
        public int read() {
          return source.take() & 0xFF;
        }
      }).getOrThrow();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] writeToByteArray(WebSocketFrame frame) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      WebSocketFrameEncoder.encodeFrame(frame, out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return out.toByteArray();
  }

  @Override
  public WebSocketFrame readFromByteArray(byte[] data) {
    try {
      return WebSocketFrameDecoder.parseFrame(new ByteArrayInputStream(data)).getOrThrow();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
