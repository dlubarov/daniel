package daniel.data.util;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @see RWUtils
 */
public final class IOUtils {
  private IOUtils() {}

  public static byte[] readFromStream(InputStream inputStream, int bytesToRead) throws IOException {
    byte[] buffer = new byte[bytesToRead];
    int nRead, nTotalRead = 0;
    while ((nRead = inputStream.read(buffer, nTotalRead, bytesToRead - nTotalRead)) != -1)
      nTotalRead += nRead;
    if (nTotalRead < bytesToRead)
      throw new EOFException(String.format(
          "End of stream was reached before %d bytes could be read.", bytesToRead));
    return buffer;
  }

  public static byte[] readEntireStream(InputStream inputStream) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[16384];
    while ((nRead = inputStream.read(data, 0, data.length)) != -1)
      buffer.write(data, 0, nRead);
    buffer.flush();
    return buffer.toByteArray();
  }

  public static byte[] readFileToByteArray(File file) throws IOException {
    return readEntireStream(new FileInputStream(file));
  }
}
