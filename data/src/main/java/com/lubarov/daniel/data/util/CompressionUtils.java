package com.lubarov.daniel.data.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

public final class CompressionUtils {
  private CompressionUtils() {}

  public static byte[] gzip(byte[] data) {
    try {
      InputStream input = new ByteArrayInputStream(data);
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      GZIPOutputStream gzipOS = new GZIPOutputStream(result);

      IOUtils.copyStream(input, gzipOS);
      gzipOS.close();
      return result.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
