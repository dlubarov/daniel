package daniel.multiweb;

import daniel.web.http.server.HttpServer;

public class MultiwebApp {
  private MultiwebApp() {}

  public static void main(String[] args) throws Exception {
    HttpServer server = new HttpServer.Builder()
        .setHandler(MultiwebHandler.getHandler())
        .setPort(12345)
        .build();
    server.start();
    server.join();
  }
}
