package daniel.blog;

public final class Config {
  private Config() {}

  public static final String homeUrl = "http://localhost:12345/";

  public static String getDatabaseHome(String dbName) {
    return "/Users/dlubarov/Development/daniel/blog/db/" + dbName;
  }
}
