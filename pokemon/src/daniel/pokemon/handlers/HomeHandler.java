package daniel.pokemon.handlers;

import daniel.data.option.Option;
import daniel.pokemon.Layout;
import daniel.web.html.Element;
import daniel.web.html.ParagraphBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;

final class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  private static final String INTRO = "Welcome to the Pokemon database!";

  private HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Element intro = new ParagraphBuilder().addRawText(INTRO).build();
    Element document = Layout.createDocument(request, Option.<String>none(), intro);
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
