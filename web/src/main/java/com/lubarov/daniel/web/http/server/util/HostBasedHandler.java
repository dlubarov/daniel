package com.lubarov.daniel.web.http.server.util;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.sequence.ImmutableSequence;
import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.server.Handler;

public final class HostBasedHandler implements Handler {
  public static final class Builder {
    private final MutableStack<KeyValuePair<String, Handler>> handlersByHostRegex =
        DynamicArray.create();

    public Builder addHandlerForHost(String hostRegex, Handler handler) {
      handlersByHostRegex.pushBack(new KeyValuePair<>(hostRegex, handler));
      return this;
    }

    public HostBasedHandler build() {
      return new HostBasedHandler(this);
    }
  }

  private final ImmutableSequence<KeyValuePair<String, Handler>> handlersByHostRegex;

  private HostBasedHandler(Builder builder) {
    handlersByHostRegex = builder.handlersByHostRegex.toImmutable();
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    String host = request.getHost().toLowerCase();
    for (KeyValuePair<String, Handler> hostRegexAndHandler : handlersByHostRegex)
      if (host.matches(hostRegexAndHandler.getKey()))
        return hostRegexAndHandler.getValue().handle(request);
    throw new IllegalArgumentException(String.format("No handler for %s.", host));
  }
}
