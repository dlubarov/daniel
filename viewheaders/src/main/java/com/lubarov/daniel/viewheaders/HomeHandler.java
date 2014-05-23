package com.lubarov.daniel.viewheaders;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.table.sequential.SequentialTable;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.TextNode;
import com.lubarov.daniel.web.html.table.TableBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  private HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Element html = Layout.createDocument(getHeaderTable(request.getHeaders()));
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html));
  }

  private Element getHeaderTable(SequentialTable<String, String> headers) {
    TableBuilder tableBuilder = new TableBuilder()
        .setTableAttribute(Attribute.CLASS, "hairlined");

    tableBuilder.beginTableHead();
    tableBuilder.beginRow();
    tableBuilder.addHeaderEntry(TextNode.escapedText("Name"));
    tableBuilder.addHeaderEntry(TextNode.escapedText("Value"));
    tableBuilder.endRow();
    tableBuilder.endTableHead();

    tableBuilder.beginTableBody();
    for (KeyValuePair<String, String> header : headers) {
      tableBuilder.beginRow();
      tableBuilder.addNormalEntry(TextNode.escapedText(header.getKey()));
      tableBuilder.addNormalEntry(TextNode.escapedText(header.getValue()));
      tableBuilder.endRow();
    }
    tableBuilder.endTableBody();

    return tableBuilder.build();
  }
}
