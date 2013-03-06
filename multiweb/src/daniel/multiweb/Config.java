package daniel.multiweb;

final class Config {
  private Config() {}

  public static int getPort() {
    return inDevMode() ? 12345 : 80;
  }

  private static boolean inDevMode() {
    return System.getenv("ENVIRONMENT").equals("development");
  }
}
