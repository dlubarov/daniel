package com.lubarov.daniel.conniewedding.admin;

import com.lubarov.daniel.conniewedding.WeddingLayout;
import com.lubarov.daniel.conniewedding.rsvp.RSVP;
import com.lubarov.daniel.conniewedding.rsvp.RSVPStorage;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.util.Check;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.TextNode;
import com.lubarov.daniel.web.html.table.TableBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

final class AdminDashboardHandler implements Handler {
  public static final AdminDashboardHandler singleton = new AdminDashboardHandler();
  private static final DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

  private AdminDashboardHandler() {}

  @Override
  public HttpResponse handle(HttpRequest request) {
    Check.that(Authenticator.isAdmin(request));

    TableBuilder tableBuilder = new TableBuilder()
        .setTableAttribute(Attribute.CLASS, "hairline")
        .beginTableHead()
        .beginRow()
        .addHeaderEntry(TextNode.escapedText("Date"))
        .addHeaderEntry(TextNode.escapedText("Name"))
        .addHeaderEntry(TextNode.escapedText("Email/Phone"))
        .addHeaderEntry(TextNode.escapedText("Attending?"))
        .addHeaderEntry(TextNode.escapedText("Party Size"))
        .addHeaderEntry(TextNode.escapedText("Entrees"))
        .addHeaderEntry(TextNode.escapedText("Kids"))
        .endRow()
        .endTableHead()
        .beginTableBody();

    for (RSVP rsvp : RSVPStorage.getAllRSVPs().sorted(RSVP.ASCENDING_CREATED_AT_ORDERING)) {
      tableBuilder.beginRow()
          .addNormalEntry(TextNode.escapedText(dateFormat.format(rsvp.createdAt.toDate())))
          .addNormalEntry(TextNode.escapedText(rsvp.name))
          .addNormalEntry(TextNode.escapedText(rsvp.emailOrPhone))
          .addNormalEntry(TextNode.escapedText(rsvp.attending ? "yes" : "no"))
          .addNormalEntry(TextNode.escapedText(rsvp.partySize))
          .addNormalEntry(TextNode.escapedText(rsvp.entrees))
          .addNormalEntry(TextNode.escapedText(rsvp.kids))
          .endRow();
    }

    Element table = tableBuilder.endTableBody().build();
    Element document = WeddingLayout.createDocument(Option.some("RSVPs"), table);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }
}
