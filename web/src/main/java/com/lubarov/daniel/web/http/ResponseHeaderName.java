package com.lubarov.daniel.web.http;

public enum ResponseHeaderName {
  ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
  ACCEPT_RANGES("Accept-Ranges"),
  AGE("Age"),
  ALLOW("Allow"),
  CACHE_CONTROL("Cache-Control"),
  CONNECTION("Connection"),
  CONTENT_ENCODING("Content-Encoding"),
  CONTENT_LANGUAGE("Content-Language"),
  CONTENT_LENGTH("Content-Length"),
  CONTENT_LOCATION("Content-Location"),
  CONTENT_MD5("Content-MD5"),
  CONTENT_DISPOSITION("Content-Disposition"),
  CONTENT_RANGE("Content-Range"),
  CONTENT_TYPE("Content-Type"),
  DATE("Date"),
  ETAG("ETag"),
  EXPIRES("Expires"),
  LAST_MODIFIED("Last-Modified"),
  LINK("Link"),
  LOCATION("Location"),
  P3P("P3P"),
  PRAGMA("Pragma"),
  PROXY_AUTHENTICATE("Proxy-Authenticate"),
  REFRESH("Refresh"),
  RETRY_AFTER("Retry-After"),
  SERVER("Server"),
  SET_COOKIE("Set-Cookie"),
  STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),
  TRAILER("Trailer"),
  TRANSFER_ENCODING("Transfer-Encoding"),
  VARY("Vary"),
  VIA("Via"),
  WARNING("Warning"),
  WWW_AUTHENTICATE("WWW-Authenticate");

  private final String standardName;

  private ResponseHeaderName(String standardName) {
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
