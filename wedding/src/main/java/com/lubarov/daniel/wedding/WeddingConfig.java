package com.lubarov.daniel.wedding;

import com.lubarov.daniel.common.Environment;

public class WeddingConfig {
  private WeddingConfig() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://danielvi.com.localhost:12345"
        : "http://danielvi.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/wedding/src/main/resources/";
  }
}
