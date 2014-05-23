package com.lubarov.daniel.data.serialization;

/**
 * Like a {@code Source<Byte>}, except that it avoids Byte objects for performance.
 */
public final class ByteSource {
  private final byte[] data;
  private int pos = 0;

  public ByteSource(byte[] data) {
    this.data = data.clone();
  }

  public boolean hasNext() {
    return pos < data.length;
  }

  public byte take() {
    return data[pos++];
  }

  public byte[] takeMany(int n) {
    byte[] bytes = new byte[n];
    System.arraycopy(data, pos, bytes, 0, n);
    pos += n;
    return bytes;
  }
}
