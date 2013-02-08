package daniel.cms;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return inDevMode()
        ? "http://acquisitive.net.wopr.local:12345"
        : "http://acquisitive.net";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/cms/static/";
  }

  public static String getDatabaseHome(String dbName) {
    return System.getProperty("user.dir") + "/cms/db/" + dbName;
  }

  private static boolean inDevMode() {
    return System.getenv("ENVIRONMENT").equals("development");
  }
}
