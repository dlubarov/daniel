package daniel.data.util;

import java.io.IOException;
import java.io.Reader;

/**
 * @see IOUtils
 */
public final class RWUtils {
  private RWUtils() {}

  public static String readUntilEnd(Reader reader) throws IOException {
    StringBuilder sb = new StringBuilder();
    int nRead;
    char[] data = new char[16384];
    while ((nRead = reader.read(data, 0, data.length)) != -1)
      sb.append(data, 0, nRead);
    return sb.toString();
  }
}
