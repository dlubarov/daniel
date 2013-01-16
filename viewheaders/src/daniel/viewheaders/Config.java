package daniel.viewheaders;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return inDevMode()
        ? "http://viewheaders.com.wopr.local:12345"
        : "http://viewheaders.com";
  }

  public static String getStaticContentRoot() {
    return inDevMode()
        ? "/Users/dlubarov/Development/daniel/viewheaders/static/"
        : "";
  }

  private static boolean inDevMode() {
    return System.getenv("environment").equals("development");
  }
}
