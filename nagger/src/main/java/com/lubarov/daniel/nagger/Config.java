package com.lubarov.daniel.nagger;

import com.lubarov.daniel.common.Environment;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://nagger.daniel.lubarov.localhost:12345"
        : "http://nagger.daniel.lubarov.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/nagger/src/main/resources/";
  }

  public static String getDatabaseHome(String dbName) {
    return System.getProperty("user.dir") + "/nagger/db/" + dbName;
  }
}
