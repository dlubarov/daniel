package com.lubarov.daniel.web.http;

public enum HttpVersion {
  _1_0("1.0"), _1_1("1.1");

  private final String versionString;

  private HttpVersion(String versionString) {
    this.versionString = versionString;
  }

  public static HttpVersion fromVersionString(String versionString) {
    for (HttpVersion httpVersion : values())
      if (httpVersion.getVersionString().equals(versionString))
        return httpVersion;
    throw new IllegalArgumentException(String.format("Unrecognized HTTP version: %s.", versionString));
  }

  public String getVersionString() {
    return versionString;
  }

  @Override
  public String toString() {
    return getVersionString();
  }
}
