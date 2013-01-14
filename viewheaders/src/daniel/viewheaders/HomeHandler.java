package daniel.viewheaders;

import daniel.data.option.Option;
import daniel.web.html.Attribute;
import daniel.web.html.Document;
import daniel.web.html.TableBuilder;
import daniel.web.html.TextNode;
import daniel.web.http.HttpHeader;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.HttpResponseFactory;
import daniel.web.http.server.PartialHandler;

public class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  private HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    TableBuilder tableBuilder = new TableBuilder()
        .setAttribute(Attribute.CLASS, "hairlined");
    for (HttpHeader header : request.getHeaders()) {
      tableBuilder.beginRow();
      tableBuilder.addColumn(TextNode.escapedText(header.getName()));
      tableBuilder.addColumn(TextNode.escapedText(header.getValue()));
    }
    Document document = Layout.createDocument(tableBuilder.build());
    return Option.some(HttpResponseFactory.htmlResponse(HttpStatus.OK, document));
  }
}
