package com.lubarov.daniel.data.serialization;

/**
 * Like a {@code Sink<Byte>}, except that it avoids Byte objects for performance.
 */
public final class ByteSink {
  private byte[] buffer;
  private int pos = 0;

  public ByteSink() {
    buffer = new byte[8];
  }

  public void give(byte b) {
    if (pos == buffer.length)
      expand();
    buffer[pos++] = b;
  }

  public void giveAll(byte... bytes) {
    int n = bytes.length;
    while (pos + n > buffer.length)
      expand();
    System.arraycopy(bytes, 0, buffer, pos, n);
    pos += n;
  }

  public byte[] getBytes() {
    byte[] bytes = new byte[pos];
    System.arraycopy(buffer, 0, bytes, 0, pos);
    return bytes;
  }

  private void expand() {
    byte[] newBuffer = new byte[buffer.length * 2];
    System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
    buffer = newBuffer;
  }
}
