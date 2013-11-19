package daniel.pokemon.handlers;

import daniel.data.option.Option;
import daniel.pokemon.Layout;
import daniel.pokemon.model.EggGroup;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;

final class EggGroupHandler implements PartialHandler {
  public static final EggGroupHandler singleton = new EggGroupHandler();

  private EggGroupHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    String resource = request.getResource();
    if (!resource.startsWith("/egg-groups"))
      return Option.none();

    for (EggGroup eggGroup : EggGroup.values())
      if (resource.startsWith("/egg-groups/" + UrlUtils.getUrlFriendlyString(eggGroup.toString()))) {
        Node content = TextNode.escapedText(eggGroup.toString() + " is an egg group.");
        Element document = Layout.createDocument(request, Option.<String>some(eggGroup.toString()), content);
        return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
      }

    return Option.none();
  }
}
