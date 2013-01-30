package daniel.web.http.websocket;

import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.util.Check;
import daniel.data.util.EqualsBuilder;
import daniel.data.util.HashCodeBuilder;
import daniel.data.util.ToStringBuilder;
import java.util.Arrays;

/**
 * A low-level representation of a WebSocket frame.
 */
public final class WebSocketFrame {
  public static final class Builder {
    private Option<Boolean> finalFragment = Option.none();
    private Option<WebSocketOpcode> opcode = Option.none();
    private Option<byte[]> maskingKey = Option.none();
    private Option<byte[]> payload = Option.none();

    public Builder setFinalFragment(boolean isFinalFragment) {
      this.finalFragment = Option.some(isFinalFragment);
      return this;
    }

    public Builder setOpcode(WebSocketOpcode opcode) {
      this.opcode = Option.some(opcode);
      return this;
    }

    public Builder setMaskingKey(byte[] maskingKey) {
      Check.that(maskingKey.length == 4);
      this.maskingKey = Option.some(maskingKey);
      return this;
    }

    public Builder setPayload(byte[] payload) {
      this.payload = Option.some(payload);
      return this;
    }

    public WebSocketFrame build() {
      return new WebSocketFrame(this);
    }
  }

  private final boolean finalFragment;
  private final WebSocketOpcode opcode;
  private final Option<byte[]> maskingKey;
  private final byte[] payload; // As it was sent over the wire.

  private WebSocketFrame(Builder builder) {
    finalFragment = builder.finalFragment.getOrThrow();
    opcode = builder.opcode.getOrThrow();
    maskingKey = builder.maskingKey;
    payload = builder.payload.getOrThrow();
  }

  public boolean isFinalFragment() {
    return finalFragment;
  }

  public WebSocketOpcode getOpcode() {
    return opcode;
  }

  public Option<byte[]> getMaskingKey() {
    return maskingKey;
  }

  public byte[] getPayload() {
    return payload;
  }

  public byte[] getUnmaskedPayload() {
    if (maskingKey.isEmpty())
      return payload;
    byte[] unmaskedPayload = new byte[payload.length];
    for (int i = 0; i < payload.length; ++i)
      unmaskedPayload[i] = (byte) (payload[i] ^ maskingKey.getOrThrow()[i % 4]);
    return unmaskedPayload;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof WebSocketFrame))
      return false;

    WebSocketFrame that = (WebSocketFrame) o;

    boolean maskingKeysEqual = this.maskingKey.isDefined() && that.maskingKey.isDefined()
      ? Arrays.equals(this.maskingKey.getOrThrow(), that.maskingKey.getOrThrow())
      : this.maskingKey.equals(that.maskingKey);

    return new EqualsBuilder()
        .append(this.finalFragment, that.finalFragment)
        .append(this.opcode, that.opcode)
        .append(maskingKeysEqual)
        .append(Arrays.equals(this.payload, that.payload))
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(finalFragment)
        .append(opcode)
        .append(maskingKey.isDefined() ? Arrays.hashCode(maskingKey.getOrThrow()) : null)
        .append(Arrays.hashCode(payload))
        .toHashCode();
  }

  @Override
  public String toString() {
    Option<String> maskingKeyString = maskingKey.map(new Function<byte[], String>() {
      @Override public String apply(byte[] k) {
        return Arrays.toString(k);
      }
    });

    return new ToStringBuilder(this)
        .append("finalFragment", finalFragment)
        .append("opcode", opcode)
        .append("maskingKey", maskingKeyString)
        .append("payload", Arrays.toString(payload))
        .toString();
  }
}
