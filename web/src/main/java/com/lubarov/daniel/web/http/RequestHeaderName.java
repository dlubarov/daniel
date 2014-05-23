package com.lubarov.daniel.web.http;

public enum RequestHeaderName {
  ACCEPT("Accept"),
  ACCEPT_CHARSET("Accept-Charset"),
  ACCEPT_ENCODING("Accept-Encoding"),
  ACCEPT_LANGUAGE("Accept-Language"),
  ACCEPT_DATETIME("Accept-Datetime"),
  AUTHORIZATION("Authorization"),
  CACHE_CONTROL("Cache-Control"),
  CONNECTION("Connection"),
  COOKIE("Cookie"),
  CONTENT_LENGTH("Content-Length"),
  CONTENT_MD5("Content-MD5"),
  CONTENT_TYPE("Content-Type"),
  DATE("Date"),
  EXPECT("Expect"),
  FROM("From"),
  HOST("Host"),
  IF_MATCH("If-Match"),
  IF_MODIFIED_SINCE("If-Modified-Since"),
  IF_NONE_MATCH("If-None-Match"),
  IF_RANGE("If-Range"),
  IF_UNMODIFIED_SINCE("If-Unmodified-Since"),
  MAX_FORWARDS("Max-Forwards"),
  PRAGMA("Pragma"),
  PROXY_AUTHORIZATION("Proxy-Authorization"),
  RANGE("Range"),
  REFERER("Referer"),
  TE("TE"),
  UPGRADE("Upgrade"),
  USER_AGENT("User-Agent"),
  VIA("Via"),
  WARNING("Warning");

  private final String standardName;

  private RequestHeaderName(String standardName) {
    this.standardName = standardName;
  }

  public String getStandardName() {
    return standardName;
  }

  @Override
  public String toString() {
    return getStandardName();
  }
}
