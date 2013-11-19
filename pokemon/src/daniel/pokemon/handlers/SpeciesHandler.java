package daniel.pokemon.handlers;

import daniel.data.option.Option;
import daniel.pokemon.Layout;
import daniel.pokemon.model.Species;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;

final class SpeciesHandler implements PartialHandler {
  public static final SpeciesHandler singleton = new SpeciesHandler();

  private SpeciesHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    String resource = request.getResource();
    if (!resource.startsWith("/species"))
      return Option.none();

    for (Species species : Species.values())
      if (resource.startsWith("/species/" + UrlUtils.getUrlFriendlyString(species.toString()))) {
        Node content = TextNode.escapedText(species.toString() + " is a species of Pokemon.");
        Element document = Layout.createDocument(request, Option.<String>some(species.toString()), content);
        return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
      }

    return Option.none();
  }
}
