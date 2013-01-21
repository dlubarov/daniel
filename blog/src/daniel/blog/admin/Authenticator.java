package daniel.blog.admin;

import daniel.blog.MiscStorage;
import daniel.data.collection.Collection;
import daniel.data.option.Option;
import daniel.web.http.HttpRequest;

public final class Authenticator {
  public static enum Status { AUTH_SUCCEEDED, AUTH_FAILED, AUTH_NOT_ATTEMPTED, PASSWORD_NOT_SETUP }

  private Authenticator() {}

  public static boolean isAdmin(HttpRequest request) {
    return authenticate(request) == Status.AUTH_SUCCEEDED;
  }

  public static Status authenticate(HttpRequest request) {
    Option<String> optAdminPassword = MiscStorage.tryGetAdminPassword();
    if (optAdminPassword.isEmpty())
      return Status.PASSWORD_NOT_SETUP;

    String adminPassword = optAdminPassword.getOrThrow();
    Collection<String> passwordCookieValues = request.getCookies().getValues("admin_password");
    if (passwordCookieValues.isEmpty())
      return Status.AUTH_NOT_ATTEMPTED;

    String suppliedPassword = passwordCookieValues.tryGetOnlyElement()
        .getOrThrow("Multiple passwords.");
    return suppliedPassword.equals(adminPassword)
        ? Status.AUTH_SUCCEEDED
        : Status.AUTH_FAILED;
  }
}
