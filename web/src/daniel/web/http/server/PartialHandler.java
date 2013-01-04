package daniel.web.http.server;

import daniel.data.option.Option;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;

public interface PartialHandler {
  public Option<HttpResponse> tryHandle(HttpRequest request);
}
