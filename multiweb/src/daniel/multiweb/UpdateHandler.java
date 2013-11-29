package daniel.multiweb;

import daniel.web.html.Element;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.HttpResponseFactory;

final class UpdateHandler implements Handler {
  public static final UpdateHandler singlgeton = new UpdateHandler();

  private UpdateHandler() {}

  @Override
  public HttpResponse handle(HttpRequest request) {
    UpdateTask.scheduleExecution();
    Element html = new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(getHead())
        .addChild(getBody())
        .build();
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html);
  }

  private static Element getHead() {
    return new Element(Tag.HEAD,
        new TitleBuilder().addEscapedText("Updater").build());
  }

  private static Element getBody() {
    return new Element.Builder(Tag.BODY)
        .addChild(new Element.Builder(Tag.H1)
            .addEscapedText("Self Updater")
            .build())
        .addChild(new ParagraphBuilder()
            .addEscapedText("The updater has been launched.")
            .build())
        .build();
  }
}
