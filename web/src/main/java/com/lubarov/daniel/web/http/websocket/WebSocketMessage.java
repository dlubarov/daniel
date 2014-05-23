package com.lubarov.daniel.web.http.websocket;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.serialization.ByteSink;
import com.lubarov.daniel.data.util.ToStringBuilder;

/**
 * A complete data which may have been constructed from multiple fragments.
 */
public final class WebSocketMessage {
  public static final class Builder {
    private Option<WebSocketMessageType> type;
    private Option<byte[]> data;

    public Builder setType(WebSocketMessageType type) {
      this.type = Option.some(type);
      return this;
    }

    public Builder setData(byte[] data) {
      this.data = Option.some(data);
      return this;
    }

    public WebSocketMessage build() {
      return new WebSocketMessage(this);
    }
  }

  /**
   * The type of the initial fragment. Shuold never be CONTINUATION.
   */
  private final WebSocketMessageType type;
  private final byte[] data;

  private WebSocketMessage(Builder builder) {
    type = builder.type.getOrThrow();
    data = builder.data.getOrThrow();
  }

  public static WebSocketMessage fromFragments(Sequence<WebSocketFrame> fragments) {
    ByteSink sink = new ByteSink();
    for (WebSocketFrame fragment : fragments)
      sink.giveAll(fragment.getUnmaskedPayload());
    return new Builder()
        .setType(WebSocketMessageType.fromOpcode(fragments.getFront().getOpcode()))
        .setData(sink.getBytes())
        .build();
  }

  public WebSocketMessageType getType() {
    return type;
  }

  public byte[] getData() {
    return data;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("type", type)
        .append("dataLength", data.length)
        .toString();
  }
}
