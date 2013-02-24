package daniel.blog;

import daniel.data.option.Option;
import daniel.data.unit.Instant;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.ParagraphBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;

public final class VersionHandler implements PartialHandler {
  public static final VersionHandler singleton = new VersionHandler();

  private VersionHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/version"))
      return Option.none();

    Node content = new ParagraphBuilder().addEscapedText(getVersion()).build();
    Element html = Layout.createDocument(request, Option.<String>none(), Option.<Instant>none(), content);
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html));
  }

  private static String getVersion() {
    String version = VersionHandler.class.getPackage().getImplementationVersion();
    return version != null ? version : "unknown";
  }
}
