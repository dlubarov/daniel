package daniel.blog.admin;

import daniel.data.option.Option;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.server.DelegatingHandler;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.StandardNotFoundHandler;

public class AdminHandler implements PartialHandler {
  public static final AdminHandler singleton = new AdminHandler();

  private final DelegatingHandler delegatingHandler;

  private AdminHandler() {
    delegatingHandler = new DelegatingHandler.Builder()
        .addPartialHandler(CreatePostHandler.singleton)
        .setDefaultHandler(new StandardNotFoundHandler())
        .build();
  }

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().startsWith("/admin"))
      return Option.none();

    return Option.some(delegatingHandler.handle(request));
  }
}
