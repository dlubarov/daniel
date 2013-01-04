package daniel.web.http.server;

import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;

public interface Handler {
  public HttpResponse handle(HttpRequest request);
}
