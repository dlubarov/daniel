package daniel.blog.admin;

import daniel.data.option.Option;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.DelegatingHandler;

public final class AdminHandler implements PartialHandler {
  public static final AdminHandler singleton = new AdminHandler();

  private final DelegatingHandler delegatingHandler;

  private AdminHandler() {
    delegatingHandler = new DelegatingHandler.Builder()
        .addPartialHandler(AdminDashboardHandler.singleton)
        .addPartialHandler(CreatePostHandler.singleton)
        .addPartialHandler(ReviewCommentsHandler.singleton)
        .build();
  }

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().startsWith("/admin"))
      return Option.none();

    switch (Authenticator.authenticate(request)) {
      case AUTH_SUCCEEDED:
        return Option.some(delegatingHandler.handle(request));
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
