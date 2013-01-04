package daniel.blog;

import daniel.data.option.Option;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.HttpResponseFactory;
import daniel.web.http.server.PartialHandler;

public class ViewPostHandler implements PartialHandler {
  public static ViewPostHandler singleton = new ViewPostHandler();

  private ViewPostHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().startsWith("/post"))
      return Option.none();

    Element content = new Element(Tag.P, TextNode.rawText("This is a blog post!"));
    Document document = Layout.createDocument(content);
    return Option.some(HttpResponseFactory.htmlResponse(HttpStatus.OK, document));
  }
}
