package daniel.multiweb;

import daniel.logging.Logger;
import daniel.web.http.server.HttpServer;
import daniel.web.http.server.util.ExceptionHandlingHandler;

public final class MultiwebApp {
  private static final Logger logger = Logger.forClass(MultiwebApp.class);

  private MultiwebApp() {}

  public static void main(String[] args) throws Exception {
    HttpServer server = new HttpServer.Builder()
        .setHandler(new ExceptionHandlingHandler(MultiwebHandler.singleton))
        .setWebSocketHandler(MultiwebWebSocketHandler.getHandler())
        .setPort(Config.getPort())
        .build();

    server.start();
    server.join();
    logger.info("Server has stopped. Exiting.");
  }
}
