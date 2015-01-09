package com.lubarov.daniel.gw2;

import com.lubarov.daniel.common.Environment;

public class Gw2Config {
  private Gw2Config() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://gw2stuff.net.wopr.local:12345"
        : "http://gw2stuff.net";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/gw2/src/main/resources/";
  }
}
