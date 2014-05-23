package com.lubarov.daniel.multiweb;

import com.lubarov.daniel.logging.Logger;
import com.lubarov.daniel.web.http.server.HttpServer;
import com.lubarov.daniel.web.http.server.util.ExceptionHandlingHandler;

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
