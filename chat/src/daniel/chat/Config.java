package daniel.chat;

final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return inDevMode()
        ? "http://jabberings.net.wopr.local:12345"
        : "http://jabberings.net";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/chat/static/";
  }

  private static boolean inDevMode() {
    return System.getenv("ENVIRONMENT").equals("development");
  }
}
