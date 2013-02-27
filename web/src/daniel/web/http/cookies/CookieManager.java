package daniel.web.http.cookies;

import daniel.data.collection.Collection;
import daniel.data.sequence.SinglyLinkedList;

public final class CookieManager {
  private static final ThreadLocal<SinglyLinkedList<Cookie>> cookies =
      new ThreadLocal<SinglyLinkedList<Cookie>>() {
        @Override protected SinglyLinkedList<Cookie> initialValue() {
          return SinglyLinkedList.create();
        }
      };

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
