package daniel.blog;

public final class Config {
  private Config() {}

  public static String getDatabaseHome(String dbName) {
    return "/Users/dlubarov/Development/daniel/blog/db/" + dbName;
  }

  public static String getBaseUrl() {
    return System.getenv("environment").equals("development")
        ? "http://daniel.lubarov.com.wopr.local:12345"
        : "http://daniel.lubarov.com";
  }
}
