package daniel.pokemon.handlers;

import daniel.data.option.Option;
import daniel.pokemon.Layout;
import daniel.pokemon.model.Ability;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;

final class AbilityHandler implements PartialHandler {
  public static final AbilityHandler singleton = new AbilityHandler();

  private AbilityHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    String resource = request.getResource();
    if (!resource.startsWith("/abilities"))
      return Option.none();

    for (Ability ability : Ability.values())
      if (resource.startsWith("/abilities/" + UrlUtils.getUrlFriendlyString(ability.toString()))) {
        Node content = TextNode.escapedText(ability.toString() + " is an ability.");
        Element document = Layout.createDocument(request, Option.<String>some(ability.toString()), content);
        return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
      }

    return Option.none();
  }
}
