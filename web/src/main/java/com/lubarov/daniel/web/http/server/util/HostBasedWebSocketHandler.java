package com.lubarov.daniel.web.http.server.util;

import com.lubarov.daniel.data.dictionary.ImmutableDictionary;
import com.lubarov.daniel.data.dictionary.MutableHashDictionary;
import com.lubarov.daniel.web.http.server.WebSocketHandler;
import com.lubarov.daniel.web.http.server.WebSocketManager;
import com.lubarov.daniel.web.http.websocket.WebSocketMessage;

public final class HostBasedWebSocketHandler implements WebSocketHandler {
  public static final class Builder {
    private final MutableHashDictionary<String, WebSocketHandler> handlersByHost =
        MutableHashDictionary.create();

    public Builder addHandlerForHost(String hostRegex, WebSocketHandler handler) {
      handlersByHost.put(hostRegex, handler);
      return this;
    }

    public HostBasedWebSocketHandler build() {
      return new HostBasedWebSocketHandler(this);
    }
  }

  private final ImmutableDictionary<String, WebSocketHandler> handlersByHost;

  private HostBasedWebSocketHandler(Builder builder) {
    handlersByHost = builder.handlersByHost.toImmutable();
  }

  @Override
  public void onConnect(WebSocketManager manager) {
    getDelegateFor(manager).onConnect(manager);
  }

  @Override
  public void onDisconnect(WebSocketManager manager) {
    getDelegateFor(manager).onDisconnect(manager);
  }

  @Override
  public void handle(WebSocketManager manager, WebSocketMessage message) {
    getDelegateFor(manager).handle(manager, message);
  }

  private WebSocketHandler getDelegateFor(WebSocketManager manager) {
    String host = manager.getRequest().getHost();
    for (String hostRegex : handlersByHost.getKeys())
      if (host.matches(hostRegex))
        return handlersByHost.getValue(hostRegex);
    throw new IllegalArgumentException(String.format("No handler for %s.", host));
  }
}
