package daniel.viewheaders;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.multidictionary.sequential.SequentialMultidictionary;
import daniel.data.option.Option;
import daniel.web.html.Attribute;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.TableBuilder;
import daniel.web.html.TextNode;
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

    Document document = Layout.createDocument(getHeaderTable(request.getHeaders()));
    return Option.some(HttpResponseFactory.htmlResponse(HttpStatus.OK, document));
  }

  private Element getHeaderTable(SequentialMultidictionary<String, String> headers) {
    TableBuilder tableBuilder = new TableBuilder()
        .setAttribute(Attribute.CLASS, "hairlined");

    tableBuilder.beginRow();
    tableBuilder.addHeaderEntry(TextNode.escapedText("Name"));
    tableBuilder.addHeaderEntry(TextNode.escapedText("Value"));
    tableBuilder.endRow();

    for (KeyValuePair<String, String> header : headers) {
      tableBuilder.beginRow();
      tableBuilder.addEntry(TextNode.escapedText(header.getKey()));
      tableBuilder.addEntry(TextNode.escapedText(header.getValue()));
      tableBuilder.endRow();
    }

    return tableBuilder.build();
  }
}
