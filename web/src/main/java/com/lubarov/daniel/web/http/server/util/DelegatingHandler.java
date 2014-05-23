package com.lubarov.daniel.web.http.server.util;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableSequence;
import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.PartialHandler;

public final class DelegatingHandler implements Handler {
  public static final class Builder {
    private final MutableStack<PartialHandler> partialHandlers = DynamicArray.create();
    private Option<Handler> defaultHandler = Option.none();

    public Builder addPartialHandler(PartialHandler partialHandler) {
      partialHandlers.pushBack(partialHandler);
      return this;
    }

    public Builder setDefaultHandler(Handler defaultHandler) {
      this.defaultHandler = Option.some(defaultHandler);
      return this;
    }

    public DelegatingHandler build() {
      return new DelegatingHandler(this);
    }
  }

  private final ImmutableSequence<PartialHandler> partialHandlers;
  private final Handler defaultHandler;

  private DelegatingHandler(Builder builder) {
    partialHandlers = builder.partialHandlers.toImmutable();
    defaultHandler = builder.defaultHandler.getOrDefault(StandardNotFoundHandler.singleton);
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    for (PartialHandler partialHandler : partialHandlers) {
      Option<HttpResponse> optResopnse = partialHandler.tryHandle(request);
      if (optResopnse.isDefined())
        return optResopnse.getOrThrow();
    }
    return defaultHandler.handle(request);
  }
}
