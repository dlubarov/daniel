package com.lubarov.daniel.web.http.server;

import com.lubarov.daniel.common.Logger;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.util.Check;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class HttpServer {
  private static final Logger logger = Logger.forClass(HttpServer.class);

  public static final class Builder {
    private Option<Handler> handler = Option.none();
    private Option<WebSocketHandler> webSocketHandler = Option.none();
    private Option<Executor> executor = Option.none();
    private Option<Integer> port = Option.none();

    public Builder setHandler(Handler handler) {
      this.handler = Option.some(handler);
      return this;
    }

    public Builder setWebSocketHandler(WebSocketHandler webSocketHandler) {
      this.webSocketHandler = Option.some(webSocketHandler);
      return this;
    }

    public Builder setExecutor(Executor executor) {
      this.executor = Option.some(executor);
      return this;
    }

    public Builder setPort(int port) {
      this.port = Option.some(port);
      return this;
    }

    public HttpServer build() {
      return new HttpServer(this);
    }
  }

  private final Handler handler;
  private final Option<WebSocketHandler> webSocketHandler;
  private final Executor executor;
  private final int port;
  private Option<ConnectionListener> connectionListener;
  private volatile Status status = Status.NOT_STARTED;

  private HttpServer(Builder builder) {
    handler = builder.handler.getOrThrow("No handler was specified.");
    webSocketHandler = builder.webSocketHandler;
    executor = builder.executor.getOrDefault(Executors.newCachedThreadPool(
        r -> new Thread(r, "connection manager")));
    port = builder.port.getOrDefault(80);
  }

  public void start() throws IOException {
    logger.info("Starting server.");
    Check.that(status == Status.NOT_STARTED, "Server was already started.");

    connectionListener = Option.some(new ConnectionListener());
    connectionListener.getOrThrow().start();
    status = Status.RUNNING;
  }

  public void join() throws InterruptedException {
    Check.that(status == Status.RUNNING, "Server is not running.");
    connectionListener.getOrThrow().join();
  }

  public void stopGracefully() {
    Check.that(status == Status.RUNNING, "Server is not running.");
    status = Status.STOPPED;
  }

  private enum Status {
    NOT_STARTED, RUNNING, STOPPED
  }

  private final class ConnectionListener extends Thread {
    private final ServerSocket serverSocket;

    private ConnectionListener() throws IOException {
      serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
      do {
        try {
          Socket socket = serverSocket.accept();
          executor.execute(new ConnectionManager(socket, handler, webSocketHandler));
        } catch (IOException e) {
          logger.error(e, "Failed to accept connection.");
        }
      } while (status != Status.STOPPED);
    }
  }
}
