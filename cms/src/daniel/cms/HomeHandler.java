package daniel.cms;

import daniel.data.option.Option;
import daniel.web.html.Element;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;

public final class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  private HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Element html = Layout.createHtml(TextNode.escapedText("Hello."));
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html));
  }
}
