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

public class CreatePostHandler implements PartialHandler {
  public static final CreatePostHandler singleton = new CreatePostHandler();

  private CreatePostHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/admin/create-post")) {
      return Option.none();
    }

    switch (request.getMethod()) {
      case GET:
      case HEAD:
        return Option.some(handleGet(request));
      case POST:
        return Option.some(handlePost(request));
      default:
        return Option.none();
    }
  }

  private static HttpResponse handleGet(HttpRequest request) {
    Document document = Layout.createDocument(TextNode.escapedText("hi"));
    return HttpResponseFactory.htmlResponse(HttpStatus.OK, document);
  }

  private static HttpResponse handlePost(HttpRequest request) {
    throw new UnsupportedOperationException("TODO: Implement this");
  }
}
