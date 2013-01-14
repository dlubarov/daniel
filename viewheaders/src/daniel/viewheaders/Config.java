package daniel.viewheaders;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return System.getenv("environment").equals("development")
        ? "http://viewheaders.com.wopr.local:12345"
        : "http://viewheaders.com";
  }
}
