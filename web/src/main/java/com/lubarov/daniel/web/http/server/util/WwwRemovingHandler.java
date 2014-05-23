package com.lubarov.daniel.web.http.server.util;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.ImmutableSequence;
import com.lubarov.daniel.common.Logger;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.RequestMethod;
import com.lubarov.daniel.web.http.server.PartialHandler;

public final class WwwRemovingHandler implements PartialHandler {
  private static final Logger logger = Logger.forClass(WwwRemovingHandler.class);

  public static final WwwRemovingHandler singleton = new WwwRemovingHandler();

  private static final ImmutableSequence<RequestMethod> methodToRedirect =
      ImmutableArray.create(RequestMethod.GET, RequestMethod.HEAD);

  private WwwRemovingHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getHost().startsWith("www."))
      return Option.none();
    if (!methodToRedirect.contains(request.getMethod()))
      return Option.none();

    String location = String.format("http://%s%s",
        request.getHost().substring(4),
        request.getResource());
    logger.info("Redirecting to %s.", location);
    return Option.some(HttpResponseFactory.permanentRedirect(location));
  }
}
