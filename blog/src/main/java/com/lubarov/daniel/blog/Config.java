package com.lubarov.daniel.blog;

import com.lubarov.daniel.common.Environment;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://daniel.lubarov.com.localhost:12345"
        : "http://daniel.lubarov.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/blog/src/main/resources/";
  }

  public static String getDatabaseHome(String dbName) {
    return System.getProperty("user.dir") + "/blog/db/" + dbName;
  }
}
