package com.lubarov.daniel.alexresume;

final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return inDevMode()
        ? "http://alex.lubarov.com.wopr.local:12345"
        : "http://alex.lubarov.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/alex-resume/src/main/resources/";
  }

  private static boolean inDevMode() {
    return System.getenv("ENVIRONMENT").equals("development");
  }
}
