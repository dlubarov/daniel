package daniel.blog;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return System.getenv("environment").equals("development")
        ? "http://daniel.lubarov.com.wopr.local:12345"
        : "http://daniel.lubarov.com";
  }

  public static String getStaticContentRoot() {
    return inDevMode()
        ? "/Users/dlubarov/Development/daniel/blog/static/"
        : "";
  }

  public static String getDatabaseHome(String dbName) {
    return inDevMode()
        ? "/Users/dlubarov/Development/daniel/blog/db/" + dbName
        : "";
  }

  private static boolean inDevMode() {
    return System.getenv("environment").equals("development");
  }
}
