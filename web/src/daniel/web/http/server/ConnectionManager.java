package daniel.web.http.server;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.option.Option;
import daniel.logging.Logger;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.RequestMethod;
import daniel.web.http.cookies.CookieManager;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

final class ConnectionManager implements Runnable {
  private static final Logger logger = Logger.forClass(ConnectionManager.class);

  private final Socket socket;
  private final Handler handler;

  public ConnectionManager(Socket socket, Handler handler) {
    this.socket = socket;
    this.handler = handler;
  }

  @Override
  public void run() {
    try {
      runWithExceptions();
    } catch (Exception e) {
      logger.error(e, "Exception in connection management.");
    }
  }

  private void runWithExceptions() throws IOException {
    Option<HttpRequest> optRequest = RequestReader.readRequest(socket.getInputStream());
    if (optRequest.isEmpty())
      return;

    HttpRequest request = optRequest.getOrThrow();
    Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);

    logger.info("Handling request for %s%s.",
        request.getHost(), request.getResource());
    CookieManager.resetCookies();
    HttpResponse response = handler.handle(request);

    writer.write(String.format("HTTP/%s %s\r\n", response.getHttpVersion(), response.getStatus()));
    for (KeyValuePair<String, String> header : response.getHeaders())
      writer.write(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
    writer.write("Connection: close\r\n");
    writer.write("\r\n");
    writer.flush();
    if (response.getBody().isDefined() && request.getMethod() != RequestMethod.HEAD)
      socket.getOutputStream().write(response.getBody().getOrThrow());
    socket.getOutputStream().flush();
    socket.close();
  }
}
