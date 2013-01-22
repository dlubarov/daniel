package daniel.multiweb;

import daniel.web.http.server.HttpServer;

public final class MultiwebApp {
  private MultiwebApp() {}

  public static void main(String[] args) throws Exception {
    HttpServer server = new HttpServer.Builder()
        .setHandler(MultiwebHandler.getHandler())
        .setPort(Config.getPort())
        .build();
    server.start();
    server.join();
    System.out.println("Server has stopped; exiting.");
  }
}
