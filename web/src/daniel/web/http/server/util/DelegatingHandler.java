package daniel.web.http.server.util;

import daniel.data.option.Option;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.server.Handler;
import daniel.web.http.server.PartialHandler;

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
