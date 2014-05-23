package com.lubarov.daniel.alexresume;

import com.lubarov.daniel.common.Environment;

final class Config {
  private Config() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://alex.lubarov.com.wopr.local:12345"
        : "http://alex.lubarov.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/alex-resume/src/main/resources/";
  }
}
