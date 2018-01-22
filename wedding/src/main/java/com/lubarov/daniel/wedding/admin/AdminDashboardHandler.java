package com.lubarov.daniel.wedding.admin;

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
import com.lubarov.daniel.wedding.WeddingLayout;
import com.lubarov.daniel.wedding.rsvp.RSVP;
import com.lubarov.daniel.wedding.rsvp.RSVPStorage;

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
        .addHeaderEntry(TextNode.escapedText("Email"))
        .addHeaderEntry(TextNode.escapedText("Attending?"))
        .addHeaderEntry(TextNode.escapedText("Entree"))
        .addHeaderEntry(TextNode.escapedText("Guest?"))
        .addHeaderEntry(TextNode.escapedText("Guest Name"))
        .addHeaderEntry(TextNode.escapedText("Guest Entree"))
        .addHeaderEntry(TextNode.escapedText("Notes"))
        .endRow()
        .endTableHead()
        .beginTableBody();

    for (RSVP rsvp : RSVPStorage.getAllRSVPs().sorted(RSVP.ASCENDING_CREATED_AT_ORDERING)) {
      tableBuilder.beginRow()
          .addNormalEntry(TextNode.escapedText(dateFormat.format(rsvp.createdAt.toDate())))
          .addNormalEntry(TextNode.escapedText(rsvp.name))
          .addNormalEntry(TextNode.escapedText(rsvp.email))
          .addNormalEntry(TextNode.escapedText(rsvp.attending ? "yes" : "no"))
          .addNormalEntry(TextNode.escapedText(rsvp.entree))
          .addNormalEntry(TextNode.escapedText(rsvp.guestAttending ? "yes" : "no"))
          .addNormalEntry(TextNode.escapedText(rsvp.guestName))
          .addNormalEntry(TextNode.escapedText(rsvp.guestEntree))
          .addNormalEntry(TextNode.escapedText(rsvp.notes))
          .endRow();
    }

    Element table = tableBuilder.endTableBody().build();
    Element document = WeddingLayout.createDocument(Option.some("RSVPs"), table);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }
}
