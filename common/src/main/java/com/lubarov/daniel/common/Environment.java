package com.lubarov.daniel.common;

import com.lubarov.daniel.data.option.Option;

public enum Environment {
  DEVELOPMENT,
  PRODUCTION;

  public static Environment get() {
    Option<String> rawEnv = Option.fromNullable(System.getenv("ENVIRONMENT"));
    return Environment.valueOf(rawEnv.getOrDefault("DEVELOPMENT"));
  }
}
