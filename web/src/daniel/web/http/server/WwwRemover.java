package daniel.web.http.server;

import daniel.data.option.Option;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.ImmutableSequence;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.RequestMethod;

public final class WwwRemover implements PartialHandler {
  public static final WwwRemover singleton = new WwwRemover();

  private static final ImmutableSequence<RequestMethod> methodToRedirect =
      ImmutableArray.create(RequestMethod.GET, RequestMethod.HEAD);

  private WwwRemover() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getHost().startsWith("www."))
      return Option.none();
    if (!methodToRedirect.contains(request.getMethod()))
      return Option.none();

    String location = String.format("http://%s%s",
        request.getHost().substring(4),
        request.getResource());
    System.out.printf("Redirecting to %s\n", location);
    return Option.some(HttpResponseFactory.redirect(location, false));
  }
}
