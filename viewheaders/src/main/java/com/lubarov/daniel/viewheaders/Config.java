package com.lubarov.daniel.viewheaders;

import com.lubarov.daniel.common.Environment;

public final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://viewheaders.com.localhost:12345"
        : "http://viewheaders.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/viewheaders/src/main/resources/";
  }
}
