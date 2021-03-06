package com.lubarov.daniel.web.http.cookies;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.sequence.SinglyLinkedList;

public final class CookieManager {
  private static final ThreadLocal<SinglyLinkedList<Cookie>> cookies =
      ThreadLocal.withInitial(SinglyLinkedList::create);

  private CookieManager() {}

  public static void setCooke(Cookie cookie) {
    cookies.set(cookies.get().plusFront(cookie));
  }

  public static Collection<Cookie> getCookies() {
    return cookies.get();
  }

  public static void resetCookies() {
    cookies.remove();
  }
}
