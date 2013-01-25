package daniel.viewheaders;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return inDevMode()
        ? "http://viewheaders.com.wopr.local:12345"
        : "http://viewheaders.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/viewheaders/static/";
  }

  private static boolean inDevMode() {
    return System.getenv("ENVIRONMENT").equals("development");
  }
}
