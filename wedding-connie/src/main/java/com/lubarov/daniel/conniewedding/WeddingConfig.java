package com.lubarov.daniel.conniewedding;

import com.lubarov.daniel.common.Environment;

public class WeddingConfig {
  private WeddingConfig() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://leoconnie.com.localhost:12345"
        : "http://leoconnie.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/wedding-connie/src/main/resources/";
  }

  public static String getDatabaseHome(String dbName) {
    return System.getProperty("user.dir") + "/wedding-connie/db/" + dbName;
  }
}
