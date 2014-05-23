package com.lubarov.daniel.nagger;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return inDevMode()
        ? "http://nagger.daniel.lubarov.com.wopr:12345"
        : "http://nagger.daniel.lubarov.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/nagger/src/main/resources/";
  }

  public static String getDatabaseHome(String dbName) {
    return System.getProperty("user.dir") + "/nagger/db/" + dbName;
  }

  private static boolean inDevMode() {
    return System.getenv("ENVIRONMENT").equals("development");
  }
}
