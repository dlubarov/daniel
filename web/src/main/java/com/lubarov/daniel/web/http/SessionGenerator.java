package com.lubarov.daniel.web.http;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Duration;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.http.cookies.Cookie;
import com.lubarov.daniel.web.http.cookies.CookieManager;
import com.lubarov.daniel.web.util.UuidUtils;

public final class SessionGenerator {
  private static final Duration sessionCookieDuration = Duration.fromDays(7);

  private SessionGenerator() {}

  public static String getSessionId(HttpRequest request) {
    Option<String> optSessionId = request.getCookies().getValues("session_id").tryGetOnlyElement();
    if (optSessionId.isDefined())
      return optSessionId.getOrThrow();

    for (Cookie cookie : CookieManager.getCookies())
      if (cookie.getName().equals("session_id"))
        return cookie.getValue();

    String sessionId = UuidUtils.randomAlphanumericUuid();
    Cookie cookie = new Cookie.Builder()
        .setName("session_id")
        .setValue(sessionId)
        .setExpires(Instant.now().plus(sessionCookieDuration))
        .build();
    CookieManager.setCooke(cookie);
    return sessionId;
  }
}
