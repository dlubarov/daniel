package daniel.web.http.server.util;

import daniel.data.dictionary.ImmutableDictionary;
import daniel.data.dictionary.MutableHashTable;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.server.Handler;

public final class HostBasedHandler implements Handler {
  public static final class Builder {
    private final MutableHashTable<String, Handler> handlersByHost = MutableHashTable.create();

    public Builder addHandlerForHost(String hostRegex, Handler handler) {
      handlersByHost.put(hostRegex, handler);
      return this;
    }

    public HostBasedHandler build() {
      return new HostBasedHandler(this);
    }
  }

  private final ImmutableDictionary<String, Handler> handlersByHost;

  private HostBasedHandler(Builder builder) {
    handlersByHost = builder.handlersByHost.toImmutable();
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    String host = request.getHost();
    for (String hostRegex : handlersByHost.getKeys())
      if (host.matches(hostRegex))
        return handlersByHost.getValue(hostRegex).handle(request);
    throw new IllegalArgumentException(String.format("No handler for %s.", host));
  }
}
