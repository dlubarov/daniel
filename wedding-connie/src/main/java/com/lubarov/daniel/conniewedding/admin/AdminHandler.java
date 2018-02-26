package com.lubarov.daniel.conniewedding.admin;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.server.PartialHandler;

public final class AdminHandler implements PartialHandler {
  public static final AdminHandler singleton = new AdminHandler();

  private AdminHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().startsWith("/admin"))
      return Option.none();

    switch (Authenticator.authenticate(request)) {
      case AUTH_SUCCEEDED:
        return Option.some(AdminDashboardHandler.singleton.handle(request));
      case AUTH_FAILED:
        return Option.some(new AdminLoginHandler().handle(request));
      case AUTH_NOT_ATTEMPTED:
        return Option.some(new AdminLoginHandler().handle(request));
      case PASSWORD_NOT_SETUP:
        return Option.some(AdminSetupHandler.singleton.handle(request));
      default:
        throw new AssertionError();
    }
  }
}
