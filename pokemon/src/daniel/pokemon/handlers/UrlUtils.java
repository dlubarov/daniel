package daniel.pokemon.handlers;

final class UrlUtils {
  private UrlUtils() {}

  public static String getUrlFriendlyString(String s) {
    String urlSubject = s
        .toLowerCase()
        .replaceAll("[^a-zA-Z0-9? -]", "")
        .trim()
        .replace(' ', '-');
    while (urlSubject.contains("--"))
      urlSubject = urlSubject.replaceAll("--", "-");
    return urlSubject;
  }
}
