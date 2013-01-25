package daniel.blog;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return inDevMode()
        ? "http://daniel.lubarov.com.wopr.local:12345"
        : "http://daniel.lubarov.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/blog/static/";
  }

  public static String getDatabaseHome(String dbName) {
    return System.getProperty("user.dir") + "/blog/db/" + dbName;
  }

  private static boolean inDevMode() {
    return System.getenv("ENVIRONMENT").equals("development");
  }
}