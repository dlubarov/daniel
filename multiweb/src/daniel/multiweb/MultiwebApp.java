package daniel.multiweb;

import daniel.logging.Logger;
import daniel.web.http.server.HttpServer;

public final class MultiwebApp {
  private static final Logger logger = Logger.forClass(MultiwebApp.class);

  private MultiwebApp() {}

  public static void main(String[] args) throws Exception {
    HttpServer server = new HttpServer.Builder()
        .setHandler(MultiwebHandler.getHandler())
        .setPort(Config.getPort())
        .build();
    server.start();
    server.join();
    logger.info("Server has stopped. Exiting.");
  }
}
