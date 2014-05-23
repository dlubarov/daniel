package com.lubarov.daniel.multiweb;

import com.lubarov.daniel.common.Environment;

final class Config {
  private Config() {}

  public static int getPort() {
    return Environment.get() == Environment.DEVELOPMENT ? 12345 : 80;
  }
}
