package daniel.web.http.server;

import daniel.data.option.Option;
import daniel.data.util.Check;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class HttpServer {
  public static class Builder {
    private Option<Handler> handler = Option.none();
    private Option<Executor> executor = Option.none();
    private Option<Integer> port = Option.none();

    public Builder setHandler(Handler handler) {
      this.handler = Option.some(handler);
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
  private final Executor executor;
  private final int port;
  private Option<ConnectionListener> connectionListener;
  private volatile Status status = Status.NOT_STARTED;

  private HttpServer(Builder builder) {
    handler = builder.handler.getOrThrow("No handler was specified.");
    executor = builder.executor.getOrDefault(Executors.newCachedThreadPool(new ThreadFactory() {
      @Override public Thread newThread(Runnable r) {
        return new Thread(r, "connection manager");
      }
    }));
    port = builder.port.getOrDefault(80);
  }

  public void start() throws IOException {
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

  private class ConnectionListener extends Thread {
    private final ServerSocket serverSocket;

    private ConnectionListener() throws IOException {
      serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
      while (status == Status.RUNNING) {
        try {
          Socket socket = serverSocket.accept();
          executor.execute(new ConnectionManager(socket, handler));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
