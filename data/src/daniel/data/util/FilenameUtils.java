package daniel.data.util;

import daniel.data.option.Option;

public final class FilenameUtils {
  private FilenameUtils() {}

  public static Option<String> getExtension(String fileName) {
    int pDot = fileName.lastIndexOf('.');
    int pSlash = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

    if (pDot == -1)
      return Option.none();
    if (pSlash > pDot)
      return Option.none();
    if (pDot == fileName.length() - 1)
      return Option.none();

    return Option.some(fileName.substring(pDot + 1));
  }
}
