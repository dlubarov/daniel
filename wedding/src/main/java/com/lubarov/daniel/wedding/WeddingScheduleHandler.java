package com.lubarov.daniel.wedding;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.ImmutableSequence;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.TextNode;
import com.lubarov.daniel.web.html.table.TableBuilder;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class WeddingScheduleHandler implements PartialHandler {
  public static final WeddingScheduleHandler singleton = new WeddingScheduleHandler();

  private static final ImmutableSequence<Event> EVENTS = ImmutableArray.create(
      new Event("4:30 pm", "ceremony begins"),
      new Event("5:00 pm", "reception begins with horderves"),
      new Event("6:00 pm", "dinner"),
      new Event("11:00 pm", "end")
  );

  private WeddingScheduleHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/schedule"))
      return Option.none();

    TableBuilder eventTableBuilder = new TableBuilder()
        .setTableAttribute(Attribute.ID, "events")
        .setTableAttribute(Attribute.CLASS, "hairline")
        .beginTableBody();
    for (Event event : EVENTS) {
      eventTableBuilder.beginRow()
          .addNormalEntry(TextNode.escapedText(event.time))
          .addNormalEntry(TextNode.escapedText(event.description))
          .endRow();
    }
    Element eventTable = eventTableBuilder.endTableBody().build();

    Element document = WeddingLayout.createDocument(
        Option.some("Schedule"),
        new ParagraphBuilder()
            .addEscapedText("This is a rough schedule; we will update it when it's more concrete.")
            .build(),
        eventTable);
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }

  private static class Event {
    final String time, description;

    Event(String time, String description) {
      this.time = time;
      this.description = description;
    }
  }
}
