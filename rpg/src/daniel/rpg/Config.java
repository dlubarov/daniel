package daniel.rpg;

public class Config {
  private Config() {}

  public static String getBaseUrl() {
    return inDevMode()
        ? "http://rpg.com.wopr.local:12345"
        : "http://rpg.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/rpg/static/";
  }

  private static boolean inDevMode() {
    return System.getenv("ENVIRONMENT").equals("development");
  }
}
