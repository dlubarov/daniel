package com.lubarov.daniel.chat;

import com.lubarov.daniel.common.Environment;

final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://jabberings.net.wopr.local:12345"
        : "http://jabberings.net";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/chat/src/main/resources/";
  }
}
