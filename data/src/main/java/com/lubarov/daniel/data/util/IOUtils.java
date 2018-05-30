package com.lubarov.daniel.data.util;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @see RWUtils
 */
public final class IOUtils {
  private IOUtils() {}

  public static byte[] readFromStream(InputStream inputStream, int bytesToRead) throws IOException {
    byte[] buffer = new byte[bytesToRead];
    int nTotalRead = 0;
    while (nTotalRead < bytesToRead) {
      int nRead = inputStream.read(buffer, nTotalRead, bytesToRead - nTotalRead);
      if (nRead == -1)
        throw new EOFException(String.format(
            "End of stream was reached before %d bytes could be read.", bytesToRead));
      nTotalRead += nRead;
    }
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

  public static void copyStream(InputStream input, OutputStream output) throws IOException {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = input.read(buffer)) != -1)
      output.write(buffer, 0, bytesRead);
  }
}
