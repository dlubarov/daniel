package daniel.blog.admin;

import daniel.blog.Layout;
import daniel.data.option.Option;
import daniel.web.html.Document;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.HttpResponseFactory;
import daniel.web.http.server.PartialHandler;

final class AdminDashboardHandler implements PartialHandler {
  public static final AdminDashboardHandler singleton = new AdminDashboardHandler();

  private AdminDashboardHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/admin"))
      return Option.none();

    Document document = Layout.createDocument(request,
        Option.some("Admin Control Panel"), Option.<String>none(),
        TextNode.escapedText("What's up?"));
    return Option.some(HttpResponseFactory.htmlResponse(HttpStatus.OK, document));
  }
}
