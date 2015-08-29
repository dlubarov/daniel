package com.lubarov.daniel.junkmail;

import com.lubarov.daniel.common.Environment;

public class Config {
  private Config() {}

  public static String getBaseUrl() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "http://baggageman.com.localhost:12345"
        : "http://baggageman.com";
  }

  public static String getSmtpHostname() {
    return Environment.get() == Environment.DEVELOPMENT
        ? "smtp-in.baggageman.com.localhost:12345"
        : "smtp-in.baggageman.com";
  }

  public static String getStaticContentRoot() {
    return System.getProperty("user.dir") + "/blog/src/main/resources/";
  }

  public static int getSmtpPort() {
    return Environment.get() == Environment.DEVELOPMENT ? 2525 : 2525;
  }
}
