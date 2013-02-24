package daniel.web.http.websocket.serialization;

final class LengthSerializer {
  private LengthSerializer() {}

  /**
   * Encodes a length in "network" (big-endian) order.
   */
  public static byte[] encodeLength(int length, int numBytes) {
    byte[] bytes = new byte[numBytes];
    for (int i = 0; i < numBytes; ++i)
      bytes[i] = (byte) (length >> (numBytes - i - 1) * 8);
    return bytes;
  }

  /**
   * Decodes a multi-byte length given in "network" (big-endian) order.
   */
  public static int decodeLength(byte[] length) {
    int n = 0;
    for (byte b : length)
      n = n << 8 | b & 0xFF;
    return n;
  }
}
